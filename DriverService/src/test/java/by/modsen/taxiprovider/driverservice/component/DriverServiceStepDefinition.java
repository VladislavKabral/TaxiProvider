package by.modsen.taxiprovider.driverservice.component;

import by.modsen.taxiprovider.driverservice.client.RatingHttpClient;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.mapper.DriverMapper;
import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import by.modsen.taxiprovider.driverservice.service.DriversService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.validation.DriversValidator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class DriverServiceStepDefinition {

    @Mock
    private DriversRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    @Mock
    private DriversValidator driversValidator;

    @Mock
    private RatingHttpClient ratingHttpClient;

    @InjectMocks
    private DriversService driverService;

    private DriverDto driverResponse;

    private DriverProfileDto driverProfile;

    private DriverResponseDto driverResponseDto;

    private DriverListDto driverListResponse;

    private Exception exception;

    public DriverServiceStepDefinition() {
        MockitoAnnotations.openMocks(this);
    }

    @Given("The list of drivers")
    public void theListOfDrivers() {
        List<Driver> drivers = getDriverList();

        when(driverRepository.findAll()).thenReturn(drivers);

        when(driverMapper.toListDto(any())).thenReturn(getDrivers().getContent());
    }

    @When("The method findAll is called")
    public void theMethodFindAllIsCalled() {
        driverListResponse = driverService.findAll();
    }

    @Then("A list of drivers returned")
    public void aListOfDriversReturned() {
        assertEquals(driverListResponse.getContent().size(), 3);
    }

    @Given("The driver")
    public void theDriver() {
        Driver driver = getDefaultDriver();

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        when(driverMapper.toDto(driver)).thenReturn(getDriver());
    }

    @When("The method findById is called with id {long}")
    public void theMethodFindByIdIsCalled(long id) {
        try {
            driverResponse = driverService.findById(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A driver response with driver")
    public void aDriverResponseWithDriver() {
        assertEquals(driverResponse.getId(), DEFAULT_DRIVER_ID);
        assertEquals(driverResponse.getLastname(), DEFAULT_DRIVER_LASTNAME);
        assertEquals(driverResponse.getFirstname(), DEFAULT_DRIVER_FIRSTNAME);
    }

    @Given("The driver with id {long} doesn't exist")
    public void theDriverWithIdDoesntExist(long id) {
        when(driverRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Driver> driver = driverRepository.findById(id);
        assertFalse(driver.isPresent());
    }

    @Then("EntityNotFoundException is threw and response doesn't have driver with id {long}")
    public void entityNotFoundExceptionIsThrew(long id) {
        String expectedResponse = String.format(DRIVER_NOT_FOUND, id);
        String actualResponse = exception.getMessage();

        assertEquals(expectedResponse, actualResponse);
    }

    @Given("The request for saving new driver")
    public void theRequestForSavingNewDriver() {
        NewDriverDto newDriver = getRequestForSaveDriver();
        Driver driver = getDefaultDriver();

        when(driverMapper.toEntity(newDriver)).thenReturn(driver);

        when(driverRepository.save(driver)).thenReturn(driver);

        when(driverRepository.findByEmail(anyString())).thenReturn(Optional.of(driver));
    }

    @When("The method save is called")
    public void theMethodSaveIsCalled() {
        NewDriverDto newDriver = getRequestForSaveDriver();

        try {
            driverResponseDto = driverService.save(newDriver);
        } catch (EntityNotFoundException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("DriverResponse with id of created driver")
    public void driverResponseWithIdOfCreatedDriver() {
        assertEquals(driverResponseDto.getId(), DEFAULT_DRIVER_ID);
    }

    @Given("An invalid request for saving new driver")
    public void anInvalidRequestForSavingNewDriver() throws EntityValidateException {
        NewDriverDto newDriver = getRequestForSaveDriver();
        Driver driver = getDefaultDriver();

        when(driverMapper.toEntity(newDriver)).thenReturn(driver);

        when(driverRepository.findByEmail(anyString())).thenReturn(Optional.ofNullable(driver));

        doThrow(new EntityValidateException(DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS))
                .when(driversValidator)
                .validate(driver);

    }

    @Then("EntityValidateException is threw")
    public void entityValidateExceptionIsThrew() {
        String actualResponse = exception.getMessage();

        assertEquals(DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, actualResponse);
    }

    @Given("An invalid request for updating driver")
    public void anInvalidRequestForUpdatingDriver() throws EntityValidateException {
        Driver driver = getDefaultDriver();

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        doThrow(new EntityValidateException(DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS))
                .when(driversValidator)
                .validate(driver);
    }

    @Given("The valid request for updating driver")
    public void theValidRequestForUpdatingDriver() {
        Driver driver = getDefaultDriver();

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));
    }

    @When("The method update is called")
    public void theMethodUpdateIsCalled() {
        DriverDto driverDto = getDriver();

        try {
            driverResponseDto = driverService.update(driverDto.getId(), driverDto);
        } catch (EntityNotFoundException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("Driver response with id of updated driver")
    public void driverResponseWithIdOfUpdatedDriver() {
        assertEquals(driverResponseDto.getId(), DEFAULT_DRIVER_ID);
    }

    @Given("The deactivating driver")
    public void theDeactivatingDriver() {
        Driver driver = getDefaultDriver();

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));
    }

    @When("The method deactivate is called with id {long}")
    public void theMethodDeactivateIsCalled(long id) {
        try {
            driverResponseDto = driverService.deactivate(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("Driver response with id of deactivated driver")
    public void driverResponseWithIdOfDeactivatedDriver() {
        assertEquals(driverResponseDto.getId(), DEFAULT_DRIVER_ID);
    }

    @Given("The driver with driver's rating")
    public void theDriverWithDriverRating() {
        RatingDto rating = getDefaultDriverRating();
        Driver driver = getDefaultDriver();

        when(driverMapper.toDto(driver)).thenReturn(getDriver());

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        when(ratingHttpClient.getDriverRating(anyLong())).thenReturn(rating);
    }

    @When("The method getProfile is called with id {long}")
    public void theMethodGetProfileIsCalled(long id) {
        try {
            driverProfile = driverService.getDriverProfile(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("Response with profile of driver")
    public void responseWithProfileOfDriver() {
        assertEquals(driverProfile.getDriver(), getDriver());
        assertEquals(driverProfile.getRating(), DEFAULT_DRIVER_RATING);
    }
}
