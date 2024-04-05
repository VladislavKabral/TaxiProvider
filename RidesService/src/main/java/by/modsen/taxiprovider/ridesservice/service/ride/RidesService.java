package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.client.DriverHttpClient;
import by.modsen.taxiprovider.ridesservice.client.PaymentHttpClient;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.request.CustomerChargeRequestDto;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideListDto;
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
import by.modsen.taxiprovider.ridesservice.util.validation.ride.RideValidator;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

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

    private final DriverHttpClient driverHttpClient;

    private final PaymentHttpClient paymentHttpClient;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final int MINIMAL_COUNT_OF_TARGET_ADDRESSES = 1;

    private static final double STARTING_COST = 3.0;

    private static final double COST_OF_KILOMETER = 0.5;

    private static final int METERS_IN_KILOMETER = 1000;

    private static final String RIDE_CURRENCY = "USD";

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String KAFKA_TOPIC_NAME = "RIDE";

    @Transactional(readOnly = true)
    public RideListDto findAll() {
        List<Ride> rides = ridesRepository.findAll();

        return RideListDto.builder()
                .content(rideMapper.toListDto(rides))
                .build();
    }

    @Transactional(readOnly = true)
    public RideDto findById(long id) throws EntityNotFoundException {
        return rideMapper.toDto(ridesRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(RIDE_NOT_FOUND, id))));
    }

    @Transactional(readOnly = true)
    public RideListDto findByPassengerId(long passengerId) {
        List<Ride> rides = ridesRepository.findByPassengerIdAndStatus(passengerId, RIDE_STATUS_COMPLETED);

        return RideListDto.builder()
                .content(rideMapper.toListDto(rides))
                .build();
    }

    @Transactional(readOnly = true)
    public RideListDto findByDriverId(long driverId) {
        List<Ride> rides = ridesRepository.findByDriverIdAndStatus(driverId, RIDE_STATUS_COMPLETED);

        return RideListDto.builder()
                .content(rideMapper.toListDto(rides))
                .build();
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

        if (rides.isEmpty()) {
            throw new EntityNotFoundException(WAITING_RIDES_NOT_FOUND);
        }

        return rides.get(0);
    }

    @Transactional
    public RideResponseDto save(NewRideDto rideDTO) throws IOException, ParseException, DistanceCalculationException,
            EntityNotFoundException, InterruptedException, EntityValidateException {

        PromoCodeDto promoCodeDTO = null;
        if (rideDTO.getPromoCode() != null) {
            promoCodeDTO = promoCodesService.findByValue(rideDTO.getPromoCode().getValue());
        }

        Ride ride = rideMapper.toEntity(rideDTO);

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
                .destinationAddresses(destinationAddresses)
                .promoCode(promoCode)
                .build());

        ride.setStatus(RIDE_STATUS_WAITING);
        ride.setCost(rideCost);

        DriverDto driver = driverHttpClient.getFreeDrivers().getContent().get(0);
        driver.setStatus(DRIVER_STATUS_TAKEN);

        driverHttpClient.updateDriver(driver);
        ride.setDriverId(driver.getId());

        rideValidator.validate(ride);

        ridesRepository.save(ride);

        Ride createdRide = findDriverCurrentDrive(driver.getId(), RIDE_STATUS_WAITING);
        if (createdRide == null) {
            throw new EntityNotFoundException(String.format(RIDE_NOT_CREATED, driver.getId()));
        }

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(NEW_RIDE_WAS_CREATED,
                createdRide.getPassengerId(),
                createdRide.getDriverId(),
                ZonedDateTime.now(ZoneId.of("UTC"))).toString());

        return new RideResponseDto(createdRide.getId());
    }

    @Transactional
    public RideResponseDto update(RideDto rideDTO) throws EntityValidateException, EntityNotFoundException {

        rideValidator.validate(rideMapper.toEntity(rideDTO));

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

        return new RideResponseDto(ride.getId());
    }

    @Transactional
    public RideResponseDto delete(long id) throws EntityNotFoundException {
        Ride ride = ridesRepository.findById(id)
                .orElseThrow(EntityNotFoundException.entityNotFoundException(String.format(RIDE_NOT_FOUND, id)));
        ridesRepository.delete(ride);
        return new RideResponseDto(id);
    }

    @Transactional
    public RideResponseDto cancel(long passengerId) throws EntityNotFoundException {
        Ride ride = findPassengerCurrentDrive(passengerId);
        ride.setStatus(RIDE_STATUS_CANCELLED);
        ridesRepository.save(ride);

        DriverDto driver = driverHttpClient.getDriverById(ride.getDriverId());
        driver.setStatus(DRIVER_STATUS_FREE);
        driverHttpClient.updateDriver(driver);

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_CANCELLED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ZonedDateTime.now(ZoneId.of("UTC")).toString()));

        return new RideResponseDto(ride.getId());
    }

    private Ride startRide(RideDto rideDTO) throws EntityNotFoundException {
        Ride ride = findDriverCurrentDrive(rideDTO.getDriverId(), RIDE_STATUS_WAITING);
        ride.setStartedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ride.setStatus(rideDTO.getStatus());

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_STARTED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getStartedAt().toString()));

        return ride;
    }

    private Ride completeRide(RideDto rideDTO) throws EntityNotFoundException {
        Ride ride = findDriverCurrentDrive(rideDTO.getDriverId(), RIDE_STATUS_IN_PROGRESS);
        ride.setEndedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());

        if ((rideDTO.getPaymentType() != null) && (!rideDTO.getPaymentType().equals(ride.getPaymentType()))) {
            ride.setPaymentType(rideDTO.getPaymentType());
        }

        if (ride.getPaymentType().equals(PAYMENT_TYPE_CARD)) {
            paymentHttpClient.payRide(CustomerChargeRequestDto.builder()
                    .taxiUserId(ride.getPassengerId())
                    .amount(ride.getCost())
                    .currency(RIDE_CURRENCY)
                    .role(PASSENGER_ROLE_NAME)
                    .build());
        }
        DriverDto driver = driverHttpClient.getDriverById(rideDTO.getDriverId());
        driver.setStatus(DRIVER_STATUS_FREE);
        driver.setBalance(driver.getBalance().add(ride.getCost()));
        driverHttpClient.updateDriver(driver);
        ride.setStatus(rideDTO.getStatus());

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_ENDED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getEndedAt()));

        return ride;
    }

    private Ride closeRide(RideDto rideDTO) throws EntityNotFoundException {
        Ride ride = findDriverCurrentDrive(rideDTO.getDriverId(), RIDE_STATUS_IN_PROGRESS);
        ride.setEndedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());

        DriverDto driver = driverHttpClient.getDriverById(rideDTO.getDriverId());
        driver.setStatus(DRIVER_STATUS_FREE);
        driver.setBalance(driver.getBalance().add(ride.getCost()));
        driverHttpClient.updateDriver(driver);
        ride.setStatus(RIDE_STATUS_COMPLETED);

        kafkaTemplate.send(KAFKA_TOPIC_NAME, String.format(RIDE_WAS_ENDED,
                ride.getPassengerId(),
                ride.getDriverId(),
                ride.getEndedAt()));

        return ride;
    }

    public BigDecimal getRideCost(PotentialRideDto potentialRideDTO)
            throws IOException, ParseException, DistanceCalculationException, InterruptedException,
            EntityNotFoundException, EntityValidateException {

        PotentialRide potentialRide = potentialRideMapper.toEntity(potentialRideDTO);

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

        List<Address> targetAddresses = potentialRide.getDestinationAddresses();

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
}
