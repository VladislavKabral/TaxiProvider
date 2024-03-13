package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverRequestDTO;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
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
import by.modsen.taxiprovider.ridesservice.util.exception.NotEnoughFreeDriversException;
import by.modsen.taxiprovider.ridesservice.util.validation.ride.DriverRequestValidator;
import by.modsen.taxiprovider.ridesservice.util.validation.ride.RideValidator;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private final DriverRequestValidator driverRequestValidator;

    @Value("${drivers-service-host-url}")
    private String DRIVERS_SERVICE_HOST_URL;

    private static final int MINIMAL_COUNT_OF_TARGET_ADDRESSES = 1;

    private static final double STARTING_COST = 3.0;

    private static final double COST_OF_KILOMETER = 0.5;

    private static final int METERS_IN_KILOMETER = 1000;

    private final RideValidator rideValidator;

    @Transactional(readOnly = true)
    public List<RideDTO> findAll() throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findAll();

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("There aren't any rides");
        }

        return rideMapper.toListDTO(rides);
    }

    @Transactional(readOnly = true)
    public RideDTO findById(long id) throws EntityNotFoundException {
        return rideMapper.toDTO(ridesRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException("Ride with id '" + id + "' wasn't found")));
    }

    @Transactional(readOnly = true)
    public List<RideDTO> findByPassengerId(long passengerId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByPassengerIdAndStatus(passengerId, "Completed");

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("Passenger with id '" + passengerId + "' doesn't have any rides");
        }

        return rideMapper.toListDTO(rides);
    }

    @Transactional(readOnly = true)
    public List<RideDTO> findByDriverId(long driverId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByDriverIdAndStatus(driverId, "Completed");

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("Driver with id '" + driverId + "' doesn't have any rides");
        }

        return rideMapper.toListDTO(rides);
    }

    public Ride findDriverCurrentDrive(long driverId, String status) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByDriverIdAndStatus(driverId, status);

        if (rides == null) {
            throw new EntityNotFoundException("Current driver doesn't have active rides");
        }

        return rides.get(0);
    }

    @Transactional
    public void save(NewRideDTO rideDTO, PromoCodeDTO promoCodeDTO, BindingResult bindingResult) throws IOException,
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

        ride.setCost(rideCost);
        ride.setStatus("Waiting");

        DriverDTO driver = getFreeDrivers().get(0);
        driver.setStatus("Taken");

        updateDriver(driver);
        ride.setDriverId(driver.getId());
        ridesRepository.save(ride);
    }

    @Transactional
    public void update(DriverRequestDTO driverRequestDTO, BindingResult bindingResult) throws EntityValidateException,
            EntityNotFoundException {

        driverRequestValidator.validate(driverRequestDTO, bindingResult);
        handleBindingResult(bindingResult);

        Ride ride = null;
        switch (driverRequestDTO.getRideStatus()) {
            case "Start" -> {
                ride = findDriverCurrentDrive(driverRequestDTO.getDriverId(), "Waiting");
                ride.setStartedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
                ride.setStatus("In process");
                break;
            }
            case "End" -> {
                ride = findDriverCurrentDrive(driverRequestDTO.getDriverId(), "In process");
                ride.setEndedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
                DriverDTO driver = getDriverById(driverRequestDTO.getDriverId());
                driver.setStatus("Free");
                driver.setBalance(driver.getBalance().add(ride.getCost()));
                updateDriver(driver);
                ride.setStatus("Completed");
                break;
            }
        }

        ridesRepository.save(ride);
    }

    @Transactional
    public void delete(long id) throws EntityNotFoundException {
        Ride ride = ridesRepository.findById(id)
                .orElseThrow(EntityNotFoundException.entityNotFoundException("Ride with id '" + id + "' wasn't found"));

        ridesRepository.delete(ride);
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
                                Mono.error(new NotEnoughFreeDriversException()))
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
                .uri("/" + driverId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new EntityNotFoundException("Cannot find driver with id '" + driverId + "'")))
                .bodyToMono(DriverDTO.class)
                .block();
    }

    private void updateDriver(DriverDTO driverDTO) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.patch()
                .uri("/" + driverDTO.getId())
                .bodyValue(driverDTO)
                .retrieve()
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
