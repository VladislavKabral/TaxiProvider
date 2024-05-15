package by.modsen.taxiprovider.ridesservice.component;

import by.modsen.taxiprovider.ridesservice.client.DriverFeignClient;
import by.modsen.taxiprovider.ridesservice.client.PaymentFeignClient;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideListDto;
import by.modsen.taxiprovider.ridesservice.mapper.ride.RideMapper;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import by.modsen.taxiprovider.ridesservice.repository.ride.RidesRepository;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.AddressesService;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.service.ride.distance.DistanceCalculator;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.validation.ride.RideValidator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.json.simple.parser.ParseException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.util.Status.RIDE_STATUS_WAITING;
import static by.modsen.taxiprovider.ridesservice.utility.RidesTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class RidesServiceStepDefinition {

    @Mock
    private RidesRepository ridesRepository;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private RideValidator rideValidator;

    @Mock
    private AddressesService addressesService;

    @Mock
    private DistanceCalculator distanceCalculator;

    @Mock
    private DriverFeignClient driverFeignClient;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private PromoCodesService promoCodesService;

    @Mock
    private PaymentFeignClient paymentFeignClient;

    @InjectMocks
    private RidesService ridesService;

    private RideResponseDto rideResponse;

    private RideDto ride;

    private RideListDto rideListResponse;

    private long rideId;

    private Exception exception;

    private String expectedMessage;

    private NewRideDto saveRequest;

    private RideDto updateRequest;

    public RidesServiceStepDefinition() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("The list of rides")
    public void theListOfRides() {
        List<Ride> rides = getRidesList();

        when(ridesRepository.findAll()).thenReturn(rides);

        when(rideMapper.toListDto(rides)).thenReturn(getRides());
    }

    @When("Get all rides")
    public void getAllRides() {
        rideListResponse = ridesService.findAll();
    }

    @Then("A list of rides returned")
    public void aListOfRidesReturned() {
        assertEquals(1, rideListResponse.getContent().size());
    }

    @Given("The empty list of rides")
    public void theEmptyListOfRides() {
        List<Ride> rides = getEmptyRidesList();

        when(ridesRepository.findAll()).thenReturn(rides);

        when(rideMapper.toListDto(rides)).thenReturn(getEmptyRidesDtoList());
    }

    @Then("An empty list of rides returned")
    public void anEmptyListOfRidesReturned() {
        assertEquals(0, rideListResponse.getContent().size());
    }

    @Given("The list of passenger's rides")
    public void theListOfPassengerSRides() {
        List<Ride> rides = getRidesList();

        when(ridesRepository.findByPassengerIdAndStatus(DEFAULT_PASSENGER_ID, DEFAULT_RIDE_STATUS)).thenReturn(rides);

        when(rideMapper.toListDto(rides)).thenReturn(getRides());
    }

    @When("Get rides of the passenger with id {long}")
    public void getPassengersRides(long id) {
        rideListResponse = ridesService.findByPassengerId(id);
    }

    @Then("A list of passenger's rides returned")
    public void aListOfPassengerSRidesReturned() {
        assertEquals(1, rideListResponse.getContent().size());
    }

    @Given("The empty list of passenger's rides")
    public void theEmptyListOfPassengerSRides() {
        List<Ride> rides = getEmptyRidesList();

        when(ridesRepository.findByPassengerIdAndStatus(DEFAULT_NON_EXISTS_PASSENGER_ID, DEFAULT_RIDE_STATUS)).thenReturn(rides);

        when(rideMapper.toListDto(rides)).thenReturn(getEmptyRides());
    }

    @Then("An empty list of passenger's rides returned")
    public void anEmptyListOfPassengerSRidesReturned() {
        assertEquals(0, rideListResponse.getContent().size());
    }

    @Given("The list of driver's rides")
    public void theListOfDriversRides() {
        List<Ride> rides = getRidesList();

        when(ridesRepository.findByDriverIdAndStatus(DEFAULT_DRIVER_ID, DEFAULT_RIDE_STATUS)).thenReturn(rides);

        when(rideMapper.toListDto(rides)).thenReturn(getRides());
    }

    @When("Get rides of the driver with id {long}")
    public void getDriverSRides(long id) {
        rideListResponse = ridesService.findByDriverId(id);
    }

    @Then("A list of driver's rides returned")
    public void aListOfDriverSRidesReturned() {
        assertEquals(1, rideListResponse.getContent().size());
    }

    @Given("The empty list of driver's rides")
    public void theEmptyListOfDriverSRides() {
        List<Ride> rides = getEmptyRidesList();

        when(ridesRepository.findByDriverIdAndStatus(DEFAULT_NON_EXISTS_DRIVER_ID, DEFAULT_RIDE_STATUS)).thenReturn(rides);

        when(rideMapper.toListDto(rides)).thenReturn(getEmptyRides());
    }

    @Then("An empty list of driver's rides returned")
    public void anEmptyListOfDriverSRidesReturned() {
        assertEquals(0, rideListResponse.getContent().size());
    }

    @Given("The ride")
    public void theRide() {
        Ride ride = getRide();

        when(ridesRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.of(ride));

        when(rideMapper.toDto(ride)).thenReturn(getRideDto());
    }

    @When("Get ride with id {long}")
    public void getTheRide(long id) {
        try {
            ride = ridesService.findById(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with the ride")
    public void aResponseWithTheRide() {
        assertEquals(ride.getId(), DEFAULT_RIDE_ID);
        assertEquals(ride.getStatus(), DEFAULT_RIDE_STATUS);
    }

    @Given("The ride doesn't exist")
    public void theRideDoesNotExist() {
        expectedMessage = String.format(RIDE_NOT_FOUND, DEFAULT_NON_EXISTS_RIDE_ID);
        when(ridesRepository.findById(DEFAULT_RIDE_ID)).thenReturn(Optional.empty());
    }

    @Then("A response with error message")
    public void aResponseWithErrorMessage() {
        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Given("The valid save request")
    public void theValidSaveRequest() throws EntityNotFoundException {
        saveRequest = getRequestForSaveRide();

        when(rideMapper.toEntity(saveRequest)).thenReturn(getRide());

        when(driverFeignClient.getFreeDrivers().getBody()).thenReturn(getFreeDrivers());

        when(ridesRepository.findByDriverIdAndStatus(anyLong(), anyString())).thenReturn(getDriversRides());
    }

    @When("Save ride")
    public void saveRide() {
        expectedMessage = PAYMENT_TYPE_IS_INVALID;
        rideId = DEFAULT_NON_EXISTS_RIDE_ID;
        try {
            rideResponse = ridesService.save(saveRequest);
        } catch (IOException | DistanceCalculationException | EntityNotFoundException | ParseException |
                 InterruptedException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("A response with ride's id")
    public void aResponseWithRidesId() {
        assertEquals(rideId, rideResponse.getId());
    }

    @Given("The invalid save request")
    public void theInvalidSaveRequest() throws EntityValidateException, EntityNotFoundException {
        saveRequest = getInvalidRequestWithInvalidPaymentTypeForSaveRide();
        Ride ride = getInvalidRide();

        when(rideMapper.toEntity(saveRequest)).thenReturn(ride);

        when(driverFeignClient.getFreeDrivers().getBody()).thenReturn(getFreeDrivers());

        when(ridesRepository.findByDriverIdAndStatus(anyLong(), anyString())).thenReturn(getDriversRides());

        doThrow(new EntityValidateException(PAYMENT_TYPE_IS_INVALID))
                .when(rideValidator)
                .validate(ride);
    }

    @Given("The valid update request")
    public void theValidUpdateRequest() {
        updateRequest = getRequestForEditDrive();

        when(rideMapper.toEntity(updateRequest)).thenReturn(getRide());

        when(ridesRepository.findByDriverIdAndStatus(anyLong(), anyString())).thenReturn(getInProgressRides());

        when(driverFeignClient.getDriverById(anyLong()).getBody()).thenReturn(getDriver());
    }

    @When("Update ride")
    public void updateRide() {
        expectedMessage = PAYMENT_TYPE_IS_INVALID;
        rideId = DEFAULT_RIDE_ID;
        try {
            rideResponse = ridesService.update(updateRequest);
        } catch (EntityValidateException | EntityNotFoundException e) {
            exception = e;
        }
    }

    @Given("The invalid update request")
    public void theInvalidUpdateRequest() throws EntityValidateException {
        updateRequest = getInvalidRequestWithInvalidPaymentTypeForEditRide();
        Ride ride = getInvalidRide();

        when(rideMapper.toEntity(updateRequest)).thenReturn(ride);

        when(ridesRepository.findByDriverIdAndStatus(anyLong(), anyString())).thenReturn(getInProgressRides());

        when(driverFeignClient.getDriverById(anyLong()).getBody()).thenReturn(getDriver());

        doThrow(new EntityValidateException(PAYMENT_TYPE_IS_INVALID))
                .when(rideValidator)
                .validate(ride);
    }

    @Given("The cancelling ride")
    public void theCancellingRide() {
        when(ridesRepository.findByPassengerIdAndStatus(DEFAULT_PASSENGER_ID, RIDE_STATUS_WAITING))
                .thenReturn(getRidesList());

        when(driverFeignClient.getDriverById(anyLong()).getBody()).thenReturn(getDriver());
    }

    @When("Cancel ride")
    public void cancelRide() {
        expectedMessage = WAITING_RIDES_NOT_FOUND;
        rideId = DEFAULT_RIDE_ID;
        try {
            rideResponse = ridesService.cancel(DEFAULT_PASSENGER_ID);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Given("The cancelling ride doesn't exist")
    public void theCancellingRideDoesNotExist() {
        when(ridesRepository.findByPassengerIdAndStatus(DEFAULT_PASSENGER_ID, RIDE_STATUS_WAITING))
                .thenReturn(getEmptyRidesList());
    }
}
