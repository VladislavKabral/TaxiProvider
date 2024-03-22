package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.ridesservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.mapper.ride.PotentialRideMapper;
import by.modsen.taxiprovider.ridesservice.mapper.ride.RideMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import by.modsen.taxiprovider.ridesservice.repository.ride.RidesRepository;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.distance.DistanceCalculator;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.ridesservice.util.exception.NotEnoughFreeDriversException;
import by.modsen.taxiprovider.ridesservice.util.validation.ride.RideValidator;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.util.Status.*;
import static by.modsen.taxiprovider.ridesservice.util.PaymentType.*;

@Service
@RequiredArgsConstructor
public class RidesService {

    private final RidesRepository ridesRepository;

    private final PromoCodesService promoCodesService;

    private final DistanceCalculator distanceCalculator;

    private final AddressesService addressesService;

    private final RideMapper rideMapper;

    private final PromoCodeMapper promoCodeMapper;

    private final PotentialRideMapper potentialRideMapper;

    private final RideValidator rideValidator;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${drivers-service-host-url}")
    private String DRIVERS_SERVICE_HOST_URL;

    @Value("${payment-service-host-url}")
    private String PAYMENT_SERVICE_HOST_URL;

    private static final int MINIMAL_COUNT_OF_TARGET_ADDRESSES = 1;

    private static final double STARTING_COST = 3.0;

    private static final double COST_OF_KILOMETER = 0.5;

    private static final int METERS_IN_KILOMETER = 1000;

    private static final String RIDE_CURRENCY = "USD";

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String KAFKA_TOPIC_NAME = "RIDE";

    @Transactional(readOnly = true)
    public List<RideDTO> findAll() throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findAll();

        if (rides.isEmpty()) {
            throw new EntityNotFoundException(RIDES_NOT_FOUND);
        }

        return rideMapper.toListDTO(rides);
    }

    @Transactional(readOnly = true)
    public RideDTO findById(long id) throws EntityNotFoundException {
        return rideMapper.toDTO(ridesRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(RIDE_NOT_FOUND, id))));
    }

    @Transactional(readOnly = true)
    public List<RideDTO> findByPassengerId(long passengerId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByPassengerIdAndStatus(passengerId, RIDE_STATUS_COMPLETED);

        if (rides.isEmpty()) {
            throw new EntityNotFoundException(String.format(PASSENGER_RIDES_NOT_FOUND, passengerId));
        }

        return rideMapper.toListDTO(rides);
    }

    @Transactional(readOnly = true)
    public List<RideDTO> findByDriverId(long driverId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByDriverIdAndStatus(driverId, RIDE_STATUS_COMPLETED);

        if (rides.isEmpty()) {
            throw new EntityNotFoundException(String.format(DRIVER_RIDES_NOT_FOUND, driverId));
        }

        return rideMapper.toListDTO(rides);
    }

    public Ride findDriverCurrentDrive(long driverId, String status) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByDriverIdAndStatus(driverId, status);

        if (rides.isEmpty()) {
            throw new EntityNotFoundException(String.format(DRIVER_CURRENT_RIDES_NOT_FOUND, status));
        }

        return rides.get(0);
    }

    public Ride findPassengerCurrentDrive(long passengerId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByPassengerIdAndStatus(passengerId, RIDE_STATUS_WAITING);

        if (rides == null) {
            throw new EntityNotFoundException(WAITING_RIDES_NOT_FOUND);
        }

        return rides.get(0);
    }

    @Transactional
    public RideResponseDTO save(NewRideDTO rideDTO, PromoCodeDTO promoCodeDTO, BindingResult bindingResult) throws IOException,
            ParseException, DistanceCalculationException, EntityNotFoundException, InterruptedException,
            EntityValidateException {

        Ride ride = rideMapper.toEntity(rideDTO);
        handleBindingResult(bindingResult);

        PromoCode promoCode = null;
        if (promoCodeDTO != null) {
            promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        }

        Address sourceAddress = ride.getSourceAddress();

        Address existingAddress = addressesService
                .findByLatitudeAndLongitude(sourceAddress.getLatitude(), sourceAddress.getLongitude());
        if (existingAddress == null) {
            addressesService.save(sourceAddress);
        } else {
            ride.setSourceAddress(existingAddress);
        }

        List<Address> destinationAddresses = ride.getDestinationAddresses();
        for (int i = 0; i < destinationAddresses.size(); i++) {
            Address address = addressesService
                    .findByLatitudeAndLongitude(destinationAddresses.get(i).getLatitude(),
                            destinationAddresses.get(i).getLongitude());
            if (address == null) {
                addressesService.save(destinationAddresses.get(i));
            } else {
                destinationAddresses.set(i, address);
            }

            ride.setDestinationAddresses(destinationAddresses);
        }

        BigDecimal rideCost = calculatePotentialRideCost(PotentialRide.builder()
                .sourceAddress(sourceAddress)
                .targetAddresses(destinationAddresses)
                .promoCode(promoCode)
                .build());

        ride.setStatus(RIDE_STATUS_WAITING);
        ride.setCost(rideCost);

        DriverDTO driver = getFreeDrivers().get(0);
        driver.setStatus(DRIVER_STATUS_TAKEN);

        updateDriver(driver);
        ride.setDriverId(driver.getId());
        ridesRepository.save(ride);

        Ride createdRide = findDriverCurrentDrive(driver.getId(), RIDE_STATUS_WAITING);
        if (createdRide == null) {
            throw new EntityNotFoundException(String.format(RIDE_NOT_CREATED, driver.getId()));
        }

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(NEW_RIDE_WAS_CREATED,
                createdRide.getPassengerId(),
                createdRide.getDriverId(),
                ZonedDateTime.now(ZoneId.of("UTC"))).toString());

        return new RideResponseDTO(createdRide.getId());
    }

    @Transactional
    public RideResponseDTO update(RideDTO rideDTO, BindingResult bindingResult) throws EntityValidateException,
            EntityNotFoundException {

        rideValidator.validate(rideDTO, bindingResult);
        handleBindingResult(bindingResult);

        Ride ride = null;
        switch (rideDTO.getStatus()) {
            case RIDE_STATUS_IN_PROGRESS -> {
                ride = startRide(rideDTO);
                break;
            }
            case RIDE_STATUS_COMPLETED -> {
                ride = completeRide(rideDTO);
                break;
            }
            case RIDE_STATUS_PAID -> {
                ride = closeRide(rideDTO);
                break;
            }
        }

        ridesRepository.save(ride);

        return new RideResponseDTO(ride.getId());
    }

    @Transactional
    public RideResponseDTO delete(long id) throws EntityNotFoundException {
        Ride ride = ridesRepository.findById(id)
                .orElseThrow(EntityNotFoundException.entityNotFoundException(String.format(RIDE_NOT_FOUND, id)));
        ridesRepository.delete(ride);
        return new RideResponseDTO(id);
    }

    @Transactional
    public RideResponseDTO cancel(long passengerId) throws EntityNotFoundException {
        Ride ride = findPassengerCurrentDrive(passengerId);
        ride.setStatus(RIDE_STATUS_CANCELLED);
        ridesRepository.save(ride);

        DriverDTO driver = getDriverById(ride.getDriverId());
        driver.setStatus(DRIVER_STATUS_FREE);
        updateDriver(driver);

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_CANCELLED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ZonedDateTime.now(ZoneId.of("UTC")).toString()));

        return new RideResponseDTO(ride.getId());
    }

    private Ride startRide(RideDTO rideDTO) throws EntityNotFoundException {
        Ride ride = findDriverCurrentDrive(rideDTO.getDriverId(), RIDE_STATUS_WAITING);
        ride.setStartedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ride.setStatus(rideDTO.getStatus());

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_STARTED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getStartedAt().toString()));

        return ride;
    }

    private Ride completeRide(RideDTO rideDTO) throws EntityNotFoundException {
        Ride ride = findDriverCurrentDrive(rideDTO.getDriverId(), RIDE_STATUS_IN_PROGRESS);
        ride.setEndedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());

        if ((rideDTO.getPaymentType() != null) && (!rideDTO.getPaymentType().equals(ride.getPaymentType()))) {
            ride.setPaymentType(rideDTO.getPaymentType());
        }

        if (ride.getPaymentType().equals(PAYMENT_TYPE_CARD)) {
            payRide(CustomerChargeRequestDTO.builder()
                    .taxiUserId(ride.getPassengerId())
                    .amount(ride.getCost())
                    .currency(RIDE_CURRENCY)
                    .role(PASSENGER_ROLE_NAME)
                    .build());
        }
        DriverDTO driver = getDriverById(rideDTO.getDriverId());
        driver.setStatus(DRIVER_STATUS_FREE);
        driver.setBalance(driver.getBalance().add(ride.getCost()));
        updateDriver(driver);
        ride.setStatus(rideDTO.getStatus());

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_ENDED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getEndedAt()));

        return ride;
    }

    private Ride closeRide(RideDTO rideDTO) throws EntityNotFoundException {
        Ride ride = findDriverCurrentDrive(rideDTO.getDriverId(), RIDE_STATUS_IN_PROGRESS);
        ride.setEndedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());

        DriverDTO driver = getDriverById(rideDTO.getDriverId());
        driver.setStatus(DRIVER_STATUS_FREE);
        driver.setBalance(driver.getBalance().add(ride.getCost()));
        updateDriver(driver);
        ride.setStatus(RIDE_STATUS_COMPLETED);

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_ENDED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getEndedAt()));

        return ride;
    }

    public BigDecimal getPotentialRideCost(PotentialRideDTO potentialRideDTO, BindingResult bindingResult)
            throws IOException, ParseException, DistanceCalculationException, InterruptedException,
            EntityNotFoundException, EntityValidateException {

        PotentialRide potentialRide = potentialRideMapper.toEntity(potentialRideDTO);
        handleBindingResult(bindingResult);

        return calculatePotentialRideCost(potentialRide);
    }

    private BigDecimal calculatePotentialRideCost(PotentialRide potentialRide) throws IOException, ParseException, DistanceCalculationException, InterruptedException, EntityNotFoundException {
        int distance = getRideDistance(potentialRide);
        PromoCode promoCode = potentialRide.getPromoCode();
        double discount = 0.0;
        if (promoCode != null) {
            discount = promoCodesService.findByValue(promoCode.getValue()).getDiscount();
        }

        BigDecimal cost = BigDecimal.valueOf(STARTING_COST +  (double) (distance / METERS_IN_KILOMETER) * COST_OF_KILOMETER);

        return cost.subtract(cost.multiply(BigDecimal.valueOf(discount)));
    }

    private int getRideDistance(PotentialRide potentialRide) throws IOException,
            ParseException, InterruptedException, DistanceCalculationException {

        List<Address> targetAddresses = potentialRide.getTargetAddresses();

        int rideDistance =  distanceCalculator.calculate(
                potentialRide.getSourceAddress(),
                targetAddresses.get(0)
        );

        if (targetAddresses.size() != MINIMAL_COUNT_OF_TARGET_ADDRESSES) {
            for (int i = 0; i < targetAddresses.size() - 1; i++) {
                rideDistance += distanceCalculator.calculate(targetAddresses.get(i), targetAddresses.get(i + 1));
            }
        }

        return rideDistance;
    }

    private List<DriverDTO> getFreeDrivers() {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri("/free")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, DRIVERS_SERVICE_HOST_URL))))
                .bodyToFlux(DriverDTO.class)
                .collect(Collectors.toList())
                .block();
    }

    private DriverDTO getDriverById(long driverId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(String.format("/%d", driverId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                        .format(EXTERNAL_SERVICE_ERROR, DRIVERS_SERVICE_HOST_URL))))
                .bodyToMono(DriverDTO.class)
                .block();
    }

    private void updateDriver(DriverDTO driverDTO) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.patch()
                .uri(String.format("/%d", driverDTO.getId()))
                .bodyValue(driverDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, DRIVERS_SERVICE_HOST_URL))))
                .bodyToMono(String.class)
                .block();
    }

    private void payRide(CustomerChargeRequestDTO chargeRequest) {
        WebClient webClient = WebClient.builder()
                .baseUrl(PAYMENT_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.post()
                .uri("/customerCharge")
                .bodyValue(chargeRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, PAYMENT_SERVICE_HOST_URL))))
                .bodyToMono(String.class)
                .block();
    }

    private void handleBindingResult(BindingResult bindingResult) throws EntityValidateException {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();

            for (FieldError error: bindingResult.getFieldErrors()) {
                message.append(error.getDefaultMessage()).append(". ");
            }

            throw new EntityValidateException(message.toString());
        }
    }

}
