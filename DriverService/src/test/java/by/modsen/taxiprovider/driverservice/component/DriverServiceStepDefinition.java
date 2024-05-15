package by.modsen.taxiprovider.driverservice.component;

import by.modsen.taxiprovider.driverservice.client.RatingFeignClient;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.rating.TaxiUserRatingDto;
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
    private RatingFeignClient ratingFeignClient;

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

    @When("Get all drivers")
    public void getAllDrivers() {
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

    @When("Find the driver with id {long}")
    public void findTheDriverById(long id) {
        try {
            driverResponse = driverService.findById(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with driver")
    public void aResponseWithDriver() {
        assertEquals(driverResponse.getId(), DEFAULT_DRIVER_ID);
        assertEquals(driverResponse.getLastname(), DEFAULT_DRIVER_LASTNAME);
        assertEquals(driverResponse.getFirstname(), DEFAULT_DRIVER_FIRSTNAME);
    }

    @Given("The driver with id {long} doesn't exist")
    public void theDriverWithIdDoesNotExist(long id) {
        when(driverRepository.findById(id)).thenReturn(Optional.empty());
        Optional<Driver> driver = driverRepository.findById(id);
        assertFalse(driver.isPresent());
    }

    @Then("The driver with id {long} wasn't found")
    public void theDriverWasNotFound(long id) {
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

    @When("Save a new driver")
    public void saveANewDriver() {
        NewDriverDto newDriver = getRequestForSaveDriver();

        try {
            driverResponseDto = driverService.save(newDriver);
        } catch (EntityNotFoundException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("A response with id of created driver")
    public void aResponseWithIdOfCreatedDriver() {
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

    @Then("A new driver wasn't created")
    public void aNewDriverWasNotCreated() {
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

    @Then("The driver wasn't updated")
    public void theDriverWasNotUpdated() {
        String actualResponse = exception.getMessage();

        assertEquals(DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, actualResponse);
    }

    @Given("The valid request for updating driver")
    public void theValidRequestForUpdatingDriver() {
        Driver driver = getDefaultDriver();

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));
    }

    @When("Update the driver")
    public void updateTheDriver() {
        DriverDto driverDto = getDriver();

        try {
            driverResponseDto = driverService.update(driverDto.getId(), driverDto);
        } catch (EntityNotFoundException | EntityValidateException e) {
            exception = e;
        }
    }

    @Then("A response with id of updated driver")
    public void aResponseWithIdOfUpdatedDriver() {
        assertEquals(driverResponseDto.getId(), DEFAULT_DRIVER_ID);
    }

    @Given("The deactivating driver")
    public void theDeactivatingDriver() {
        Driver driver = getDefaultDriver();

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));
    }

    @When("Deactivate the driver with id {long}")
    public void deactivateTheDriverById(long id) {
        try {
            driverResponseDto = driverService.deactivate(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with id of deactivated driver")
    public void aResponseWithIdOfDeactivatedDriver() {
        assertEquals(driverResponseDto.getId(), DEFAULT_DRIVER_ID);
    }

    @Given("The driver with driver's rating")
    public void theDriverWithDriverRating() {
        TaxiUserRatingDto rating = getDefaultDriverTaxiUserRating();
        Driver driver = getDefaultDriver();

        when(driverMapper.toDto(driver)).thenReturn(getDriver());

        when(driverRepository.findById(anyLong())).thenReturn(Optional.ofNullable(driver));

        when(ratingFeignClient.getTaxiUserRating(anyLong(), anyString()).getBody()).thenReturn(rating);
    }

    @When("Get profile of the driver with id {long}")
    public void theMethodGetProfileIsCalled(long id) {
        try {
            driverProfile = driverService.getDriverProfile(id);
        } catch (EntityNotFoundException e) {
            exception = e;
        }
    }

    @Then("A response with profile of the driver")
    public void aResponseWithProfileOfDriver() {
        assertEquals(driverProfile.getDriver(), getDriver());
        assertEquals(driverProfile.getRating(), DEFAULT_DRIVER_RATING);
    }
}
