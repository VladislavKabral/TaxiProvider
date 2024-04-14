package by.modsen.taxiprovider.passengerservice.component;

import by.modsen.taxiprovider.passengerservice.client.RatingHttpClient;
import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerListDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDto;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.service.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.validation.PassengersValidator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static by.modsen.taxiprovider.passengerservice.utility.PassengersTestUtil.*;
import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class PassengerServiceStepDefinition {

    @Mock
    private PassengersRepository passengersRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PassengersValidator passengersValidator;

    @Mock
    private RatingHttpClient ratingHttpClient;

    @InjectMocks
    private PassengersService passengersService;

    private PassengerDto passengerResponse;

    private PassengerProfileDto passengerProfile;

    private PassengerResponseDto passengerResponseDto;

    private PassengerListDto passengerListResponse;

    private Exception exception;

    public PassengerServiceStepDefinition() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("The list of passengers")
    public void theListOfPassengers() {
        List<Passenger> passengers = getPassengersList();

        when(passengersRepository.findAll()).thenReturn(passengers);

        when(passengerMapper.toListDto(any())).thenReturn(getPassengers());
    }

    @When("Get all passengers")
    public void getAllPassengers() {
        passengerListResponse = passengersService.findAll();
    }

    @Then("A list of passengers returned")
    public void aListOfPassengersReturned() {
        assertEquals(passengerListResponse.getContent().size(), 3);
    }

    @Given("The passenger")
    public void thePassenger() {
        Passenger passenger = getDefaultPassenger();

        when(passengersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(passenger));

        when(passengerMapper.toDto(passenger)).thenReturn(getPassenger());
    }

    @When("Find the passenger with id {long}")
    public void findThePassengerWithId(long id) {
        try {
            passengerResponse = passengersService.findById(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with passenger")
    public void aResponseWithPassenger() {
        assertEquals(passengerResponse.getId(), DEFAULT_PASSENGER_ID);
        assertEquals(passengerResponse.getLastname(), DEFAULT_PASSENGER_LASTNAME);
        assertEquals(passengerResponse.getFirstname(), DEFAULT_PASSENGER_FIRSTNAME);
    }

    @Given("The passenger with id {long} doesn't exist")
    public void thePassengerWithIdDoeNotExist(long id) {
        when(passengersRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Passenger> passenger = passengersRepository.findById(id);
        assertFalse(passenger.isPresent());
    }

    @Then("The passenger with id {long} wasn't found")
    public void thePassengerWithIdWasNotFound(long id) {
        String expectedResponse = String.format(PASSENGER_NOT_FOUND, id);
        String actualResponse = exception.getMessage();

        assertEquals(expectedResponse, actualResponse);
    }

    @Given("The request for saving new passenger")
    public void theRequestForSavingNewPassenger() {
        NewPassengerDto newPassenger = getRequestForSavePassenger();
        Passenger passenger = getDefaultPassenger();

        when(passengerMapper.toEntity(newPassenger)).thenReturn(passenger);

        when(passengersRepository.save(passenger)).thenReturn(passenger);

        when(passengersRepository.findByEmail(anyString())).thenReturn(Optional.of(passenger));
    }

    @When("Save a new passenger")
    public void saveANewPassenger() {
        NewPassengerDto newPassenger = getRequestForSavePassenger();

        try {
            passengerResponseDto = passengersService.save(newPassenger);
        } catch (EntityNotFoundException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("A response with id of created passenger")
    public void aResponseWithIdOfCreatedPassenger() {
        assertEquals(passengerResponseDto.getId(), DEFAULT_PASSENGER_ID);
    }

    @Given("An invalid request for saving new passenger")
    public void anInvalidRequestForSavingNewPassenger() throws EntityValidateException {
        NewPassengerDto newPassenger = getRequestForSavePassenger();
        Passenger passenger = getDefaultPassenger();

        when(passengerMapper.toEntity(newPassenger)).thenReturn(passenger);

        when(passengersRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(passenger));

        doThrow(new EntityValidateException(PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS))
                .when(passengersValidator)
                .validate(passenger);
    }

    @Then("A new passenger wasn't created")
    public void aNewPassengerWasNotCreated() {
        String actualResponse = exception.getMessage();

        assertEquals(PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, actualResponse);
    }

    @Given("The valid request for updating passenger")
    public void theValidRequestForUpdatingPassenger() {
        Passenger passenger = getDefaultPassenger();
        PassengerDto passengerDto = getPassenger();

        when(passengerMapper.toEntity(passengerDto)).thenReturn(passenger);

        when(passengersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(passenger));
    }

    @When("Update the passenger")
    public void updateThePassenger() {
        PassengerDto passengerDto = getPassenger();

        try {
            passengerResponseDto = passengersService.update(passengerDto.getId(), passengerDto);
        } catch (EntityNotFoundException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("A response with id of updated passenger")
    public void aResponseWithIdOfUpdatedPassenger() {
        assertEquals(passengerResponseDto.getId(), DEFAULT_PASSENGER_ID);
    }

    @Given("An invalid request for updating passenger")
    public void anInvalidRequestForUpdatingPassenger() throws EntityValidateException {
        Passenger passenger = getDefaultPassenger();
        PassengerDto passengerDto = getPassenger();

        when(passengerMapper.toEntity(passengerDto)).thenReturn(passenger);

        when(passengersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(passenger));

        doThrow(new EntityValidateException(PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS))
                .when(passengersValidator)
                .validate(passenger);
    }

    @Then("The passenger wasn't updated")
    public void thePassengerWasNotUpdated() {
        String actualResponse = exception.getMessage();

        assertEquals(PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, actualResponse);
    }

    @Given("The deactivating passenger")
    public void theDeactivatingPassenger() {
        Passenger passenger = getDefaultPassenger();

        when(passengersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(passenger));
    }

    @When("Deactivate the passenger with id {long}")
    public void deactivateThePassengerWithId(long id) {
        try {
            passengerResponseDto = passengersService.deactivate(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with id of deactivated passenger")
    public void aResponseWithIdOfDeactivatedPassenger() {
        assertEquals(passengerResponseDto.getId(), DEFAULT_PASSENGER_ID);
    }

    @Given("The passenger with passenger's rating")
    public void thePassengerWithPassengerSRating() {
        RatingDto rating = getDefaultDriverRating();
        Passenger passenger = getDefaultPassenger();

        when(passengerMapper.toDto(passenger)).thenReturn(getPassenger());

        when(passengersRepository.findById(anyLong())).thenReturn(Optional.ofNullable(passenger));

        when(ratingHttpClient.getPassengerRating(anyLong())).thenReturn(rating);
    }

    @When("Get profile of the passenger with id {long}")
    public void getProfileOfThePassengerWithId(long id) {
        try {
            passengerProfile = passengersService.getPassengerProfile(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with profile of the passenger")
    public void aResponseWithProfileOfThePassenger() {
        assertEquals(passengerProfile.getPassenger(), getPassenger());
        assertEquals(passengerProfile.getRating(), DEFAULT_PASSENGER_RATING);
    }
}
