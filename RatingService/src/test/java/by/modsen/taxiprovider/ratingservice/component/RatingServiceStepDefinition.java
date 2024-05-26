package by.modsen.taxiprovider.ratingservice.component;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.ratingservice.mapper.RatingMapper;
import by.modsen.taxiprovider.ratingservice.repository.RatingsRepository;
import by.modsen.taxiprovider.ratingservice.service.RatingsService;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ratingservice.util.validation.RatingValidator;
import by.modsen.taxiprovider.ratingservice.util.validation.TaxiUserRequestValidator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static by.modsen.taxiprovider.ratingservice.util.Message.TAXI_USER_NOT_FOUND;
import static by.modsen.taxiprovider.ratingservice.utility.RatingsTestUtil.*;
import static by.modsen.taxiprovider.ratingservice.util.Message.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class RatingServiceStepDefinition {

    @Mock
    private RatingsRepository ratingsRepository;

    @Mock
    private RatingMapper ratingMapper;

    @Mock
    private RatingValidator ratingValidator;

    @Mock
    private TaxiUserRequestValidator taxiUserRequestValidator;

    @InjectMocks
    private RatingsService ratingsService;

    private TaxiUserRequestDto taxiUserRequest;

    private RatingDto rateRequest;

    private RatingResponseDto ratingResponse;

    private TaxiUserRatingDto taxiUserRatingResponse;

    private Exception exception;

    public RatingServiceStepDefinition() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("The request for getting driver's rating")
    public void theRequestForGettingDriverRating() {
        TaxiUserRequestDto request = getRequestForDriverRating();

        when(ratingsRepository.findByTaxiUserIdAndRole(request.getTaxiUserId(), request.getRole()))
                .thenReturn(getDriverRatings());
    }


    @When("Get a rating of the driver with id {long}")
    public void getARatingOfTheDriverWithId(long id) {
        TaxiUserRequestDto request = getRequestForDriverRating();
        request.setTaxiUserId(id);

        try {
            taxiUserRatingResponse = ratingsService.getTaxiUserRating(request);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with rating of the driver")
    public void aResponseWithRatingOfTheDriver() {
        assertEquals(taxiUserRatingResponse.getTaxiUserId(), DEFAULT_TAXI_USER_ID);
        assertEquals(taxiUserRatingResponse.getRole(), DRIVER_ROLE_NAME);
        assertEquals(taxiUserRatingResponse.getValue(), DEFAULT_RATING_VALUE);
    }

    @Given("The request for getting passenger's rating")
    public void theRequestForGettingPassengerSRating() {
        TaxiUserRequestDto request = getRequestForPassengerRating();

        when(ratingsRepository.findByTaxiUserIdAndRole(request.getTaxiUserId(), request.getRole()))
                .thenReturn(getPassengerRatings());
    }

    @When("Get a rating of the passenger with id {long}")
    public void getARatingOfThePassenger(long id) {
        TaxiUserRequestDto request = getRequestForPassengerRating();
        request.setTaxiUserId(id);

        try {
            taxiUserRatingResponse = ratingsService.getTaxiUserRating(request);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with rating of the passenger")
    public void aResponseWithRatingOfThePassenger() {
        assertEquals(taxiUserRatingResponse.getTaxiUserId(), DEFAULT_TAXI_USER_ID);
        assertEquals(taxiUserRatingResponse.getRole(), PASSENGER_ROLE_NAME);
        assertEquals(taxiUserRatingResponse.getValue(), DEFAULT_RATING_VALUE);
    }

    @Given("The request for getting non-existing driver's rating")
    public void theRequestForGettingNonExistingDriverSRating() {
        TaxiUserRequestDto request = getIncorrectRequestForDriverRating();

        when(ratingsRepository.findByTaxiUserIdAndRole(request.getTaxiUserId(), request.getRole()))
                .thenReturn(getEmptyListOfRatings());
    }

    @Then("Driver with id {long} wasn't found")
    public void driverWithIdWasNotFound(long id) {
        String expectedResponse = String.format(TAXI_USER_NOT_FOUND, id, DRIVER_ROLE_NAME);
        String actualResponse = exception.getMessage();

        assertEquals(expectedResponse, actualResponse);
    }

    @Given("The request for getting non-existing passenger's rating")
    public void theRequestForGettingNonExistingPassengerSRating() {
        TaxiUserRequestDto request = getIncorrectRequestForPassengerRating();

        when(ratingsRepository.findByTaxiUserIdAndRole(request.getTaxiUserId(), request.getRole()))
                .thenReturn(getEmptyListOfRatings());
    }

    @Then("Passenger with id {long} wasn't found")
    public void passengerWithIdWasNotFound(long id) {
        String expectedResponse = String.format(TAXI_USER_NOT_FOUND, id, PASSENGER_ROLE_NAME);
        String actualResponse = exception.getMessage();

        assertEquals(expectedResponse, actualResponse);
    }

    @Given("The request for initialization of driver's rating")
    public void theRequestForInitializationOfDriverSRating() {
        taxiUserRequest = getRequestForDriverRating();
    }

    @When("Init driver's rating")
    public void initDriverSRating() {
        try {
            ratingResponse = ratingsService.initTaxiUserRatings(taxiUserRequest);
        } catch (EntityValidateException e) {
            exception = e;
        }
    }

    @Then("Response with driver's rating")
    public void responseWithDriverSRating() {
        assertEquals(ratingResponse.getTaxiUserId(), DEFAULT_TAXI_USER_ID);
        assertEquals(ratingResponse.getRole(), DRIVER_ROLE_NAME);
        assertEquals(ratingResponse.getValue(), DEFAULT_RATING_VALUE);
    }

    @Given("The request for initialization of passenger's rating")
    public void theRequestForInitializationOfPassengerSRating() {
        taxiUserRequest = getRequestForPassengerRating();
    }

    @When("Init passenger's rating")
    public void initPassengerSRating() {
        try {
            ratingResponse = ratingsService.initTaxiUserRatings(taxiUserRequest);
        } catch (EntityValidateException e) {
            exception = e;
        }
    }

    @Then("Response with passenger's rating")
    public void responseWithPassengerSRating() {
        assertEquals(ratingResponse.getTaxiUserId(), DEFAULT_TAXI_USER_ID);
        assertEquals(ratingResponse.getRole(), PASSENGER_ROLE_NAME);
        assertEquals(ratingResponse.getValue(), DEFAULT_RATING_VALUE);
    }

    @Given("The request for rating the driver")
    public void theRequestForRatingTheDriver() {
        rateRequest = getRequestForRateDriver();

        when(ratingMapper.toEntity(rateRequest)).thenReturn(getDriverRating());
    }

    @When("Rate the driver")
    public void rateTheDriver() {
        try {
            ratingResponse = ratingsService.save(rateRequest);
        } catch (EntityValidateException e) {
            exception = e;
        }
    }

    @Then("Response with not default driver's rating")
    public void responseWithNotDefaultDriverSRating() {
        assertEquals(ratingResponse.getTaxiUserId(), DEFAULT_TAXI_USER_ID);
        assertEquals(ratingResponse.getRole(), DRIVER_ROLE_NAME);
        assertEquals(ratingResponse.getValue(), NOT_DEFAULT_RATING_VALUE);
    }

    @Given("The request for rating the passenger")
    public void theRequestForRatingThePassenger() {
        rateRequest = getRequestForRatePassenger();

        when(ratingMapper.toEntity(rateRequest)).thenReturn(getPassengersRating());
    }

    @When("Rate the passenger")
    public void rateThePassenger() {
        try {
            ratingResponse = ratingsService.save(rateRequest);
        } catch (EntityValidateException e) {
            exception = e;
        }
    }

    @Then("Response with not default passenger's rating")
    public void responseWithNotDefaultPassengerSRating() {
        assertEquals(ratingResponse.getTaxiUserId(), DEFAULT_TAXI_USER_ID);
        assertEquals(ratingResponse.getRole(), PASSENGER_ROLE_NAME);
        assertEquals(ratingResponse.getValue(), NOT_DEFAULT_RATING_VALUE);
    }

    @Given("The invalid request for rating the driver")
    public void theInvalidRequestForRatingTheDriver() throws EntityValidateException {
        rateRequest = getInvalidRequestWithInvalidRoleForRateDriver();

        doThrow(new EntityValidateException(String.format(ROLE_IS_INVALID, rateRequest.getRole())))
                .when(ratingValidator)
                .validate(rateRequest);
    }

    @Then("Response with error of validation driver's request")
    public void responseWithErrorOfValidationDriverSRequest() {
        String expectedResponse = String.format(ROLE_IS_INVALID, rateRequest.getRole());
        String actualResponse = exception.getMessage();

        assertEquals(expectedResponse, actualResponse);
    }

    @Given("The invalid request for rating the passenger")
    public void theInvalidRequestForRatingThePassenger() throws EntityValidateException {
        rateRequest = getInvalidRequestWithInvalidRatingValueForRatePassenger();

        doThrow(new EntityValidateException(String.format(ROLE_IS_INVALID, rateRequest.getRole())))
                .when(ratingValidator)
                .validate(rateRequest);
    }

    @Then("Response with error of validation passenger's request")
    public void responseWithErrorOfValidationPassengerSRequest() {
        String expectedResponse = String.format(ROLE_IS_INVALID, rateRequest.getRole());
        String actualResponse = exception.getMessage();

        assertEquals(expectedResponse, actualResponse);
    }
}
