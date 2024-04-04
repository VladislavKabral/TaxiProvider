package by.modsen.taxiprovider.driverservice.unit;

import by.modsen.taxiprovider.driverservice.controller.driver.DriversController;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriversPageDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.service.DriversService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(DriversController.class)
class DriversControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DriversService driversService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetDriversWhenDriversExistReturnListOfDrivers() throws Exception {
        //given
        DriverListDto drivers = getDrivers();

        //when
        when(driversService.findAll()).thenReturn(drivers);

        //then
        mockMvc.perform(get("/drivers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].lastname").value(drivers.getContent().get(0).getLastname()))
                .andExpect(jsonPath("$.content[1].lastname").value(drivers.getContent().get(1).getLastname()))
                .andExpect(jsonPath("$.content[2].lastname").value(drivers.getContent().get(2).getLastname()));
    }

    @Test
    public void testGetDriversWhenDriversDoNotExistReturnErrorResponse() throws Exception {
        //given
        DriverListDto drivers = getEmptyDriverList();

        //when
        when(driversService.findAll()).thenReturn(drivers);

        //then
        mockMvc.perform(get("/drivers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void testGetDriverWhenDriverExistsReturnDriverWithGivenId() throws Exception {
        //given
        DriverDto driver = getDriver();

        //when
        when(driversService.findById(DEFAULT_DRIVER_ID)).thenReturn(driver);

        //then
        mockMvc.perform(get("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(driver.getId()))
                .andExpect(jsonPath("$.lastname").value(driver.getLastname()))
                .andExpect(jsonPath("$.firstname").value(driver.getFirstname()))
                .andExpect(jsonPath("$.email").value(driver.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(driver.getPhoneNumber()))
                .andExpect(jsonPath("$.accountStatus").value(driver.getAccountStatus()))
                .andExpect(jsonPath("$.status").value(driver.getStatus()))
                .andExpect(jsonPath("$.balance").value(driver.getBalance()));
    }

    @Test
    public void testGetDriverWhenDriverWithGivenIdWasNotFoundReturnErrorResponse() throws Exception {
        //given

        //when
        when(driversService.findById(DEFAULT_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, DEFAULT_DRIVER_ID)));

        //then
        mockMvc.perform(get("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, DEFAULT_DRIVER_ID)));
    }

    @Test
    public void testGetDriversPageWhenPageAndSizeAreValidReturnDriversPage() throws Exception {
        //given
        DriversPageDto drivers = DriversPageDto.builder()
                .page(DEFAULT_PAGE)
                .size(DEFAULT_PAGE_SIZE)
                .content(getDrivers().getContent())
                .build();

        //when
        when(driversService.findPageDrivers(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD)).thenReturn(drivers);

        //then
        mockMvc.perform(get("/drivers")
                        .queryParam("page", String.valueOf(DEFAULT_PAGE))
                        .queryParam("size", String.valueOf(DEFAULT_PAGE_SIZE))
                        .queryParam("sort", DEFAULT_SORT_FIELD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].lastname").value(drivers.getContent().get(0).getLastname()))
                .andExpect(jsonPath("$.content[1].lastname").value(drivers.getContent().get(1).getLastname()))
                .andExpect(jsonPath("$.content[2].lastname").value(drivers.getContent().get(2).getLastname()));
    }

    @Test
    public void testGetDriversPageWhenThereAreNotAnyDriversOnThePageReturnErrorResponse() throws Exception {
        //given

        //when
        when(driversService.findPageDrivers(DEFAULT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT_FIELD))
                .thenThrow(new EntityNotFoundException(DRIVERS_ON_PAGE_NOT_FOUND));

        //then
        mockMvc.perform(get("/drivers")
                        .queryParam("page", String.valueOf(DEFAULT_PAGE))
                        .queryParam("size", String.valueOf(DEFAULT_PAGE_SIZE))
                        .queryParam("sort", DEFAULT_SORT_FIELD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(DRIVERS_ON_PAGE_NOT_FOUND));
    }

    @Test
    public void testGetDriversPageWhenPageAndSizeAreInvalidReturnErrorResponse() throws Exception {
        //given

        //when
        when(driversService.findPageDrivers(DEFAULT_INVALID_PAGE, DEFAULT_INVALID_PAGE_SIZE, DEFAULT_SORT_FIELD))
                .thenThrow(new InvalidRequestDataException(INVALID_PAGE_REQUEST));

        //then
        mockMvc.perform(get("/drivers")
                        .queryParam("page", String.valueOf(DEFAULT_INVALID_PAGE))
                        .queryParam("size", String.valueOf(DEFAULT_INVALID_PAGE_SIZE))
                        .queryParam("sort", DEFAULT_SORT_FIELD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_PAGE_REQUEST));
    }

    @Test
    public void testGetFreeDriversWhenFreeDriversExistReturnListOfFreeDrivers() throws Exception {
        //given
        DriverListDto drivers = getDrivers();

        //when
        when(driversService.findFreeDrivers()).thenReturn(drivers);

        //then
        mockMvc.perform(get("/drivers/free")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3))
                .andExpect(jsonPath("$.content[0].lastname").value(drivers.getContent().get(0).getLastname()))
                .andExpect(jsonPath("$.content[1].lastname").value(drivers.getContent().get(1).getLastname()))
                .andExpect(jsonPath("$.content[2].lastname").value(drivers.getContent().get(2).getLastname()));
    }

    @Test
    public void testGetFreeDriversWhenThereAreNotAnyFreeDriversReturnErrorResponse() throws Exception {
        //given
        DriverListDto drivers = getEmptyDriverList();

        //when
        when(driversService.findFreeDrivers()).thenReturn(drivers);

        //then
        mockMvc.perform(get("/drivers/free")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(0));
    }

    @Test
    public void testGetDriverProfileWhenDriverExistsReturnDriverProfile() throws Exception {
        //given
        DriverProfileDto driverProfile = getDriverProfile();

        //when
        when(driversService.getDriverProfile(DEFAULT_DRIVER_ID)).thenReturn(driverProfile);

        //then
        mockMvc.perform(get("/drivers/1/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.driver.lastname").value(driverProfile.getDriver().getLastname()))
                .andExpect(jsonPath("$.driver.firstname").value(driverProfile.getDriver().getFirstname()))
                .andExpect(jsonPath("$.driver.email").value(driverProfile.getDriver().getEmail()))
                .andExpect(jsonPath("$.rating").value(driverProfile.getRating()));
    }

    @Test
    public void testGetDriverProfileWhenDriversProfileWasNotFoundReturnErrorResponse() throws Exception {
        //given

        //when
        when(driversService.getDriverProfile(DEFAULT_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, DEFAULT_DRIVER_ID)));

        //then
        mockMvc.perform(get("/drivers/1/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, DEFAULT_DRIVER_ID)));
    }

    @Test
    public void testSaveDriverWhenRequestIsValidReturnIdOfCreatedDriver() throws Exception {
        //given
        NewDriverDto newDriverRequest = getRequestForSaveDriver();

        //when
        when(driversService.save(eq(newDriverRequest), any(BindingResult.class)))
                .thenReturn(new DriverResponseDto(DEFAULT_DRIVER_ID));

        //then
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(DEFAULT_DRIVER_ID));
    }

    @Test
    public void testSaveDriverWhenLastnameIsInvalidReturnErrorResponse() throws Exception {
        //given
        NewDriverDto newDriverRequest = getRequestForSaveDriverWithInvalidLastName();

        //when
        when(driversService.save(eq(newDriverRequest), any(BindingResult.class)))
                .thenThrow(new EntityValidateException(DRIVER_LASTNAME_BODY_IS_INVALID));

        //then
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_LASTNAME_BODY_IS_INVALID));
    }

    @Test
    public void testSaveDriverWhenFirstnameIsInvalidReturnErrorResponse() throws Exception {
        //given
        NewDriverDto newDriverRequest = getRequestForSaveDriverWithInvalidFirstName();

        //when
        when(driversService.save(eq(newDriverRequest), any(BindingResult.class)))
                .thenThrow(new EntityValidateException(DRIVER_FIRSTNAME_BODY_IS_INVALID));

        //then
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_FIRSTNAME_BODY_IS_INVALID));
    }

    @Test
    public void testSaveDriverWhenEmailIsInvalidReturnErrorResponse() throws Exception {
        //given
        NewDriverDto newDriverRequest = getRequestForSaveDriverWithInvalidEmail();

        //when
        when(driversService.save(eq(newDriverRequest), any(BindingResult.class)))
                .thenThrow(new EntityValidateException(DRIVER_EMAIL_WRONG_FORMAT));

        //then
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_EMAIL_WRONG_FORMAT));
    }

    @Test
    public void testSaveDriverWhenPhoneNumberIsInvalidReturnErrorResponse() throws Exception {
        //given
        NewDriverDto newDriverRequest = getRequestForSaveDriverWithInvalidPhoneNumber();

        //when
        when(driversService.save(eq(newDriverRequest), any(BindingResult.class)))
                .thenThrow(new EntityValidateException(DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG));

        //then
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG));
    }

    @Test
    public void testSaveDriverWhenPasswordIsInvalidReturnErrorResponse() throws Exception {
        //given
        NewDriverDto newDriverRequest = getRequestForSaveDriverWithInvalidPassword();

        //when
        when(driversService.save(eq(newDriverRequest), any(BindingResult.class)))
                .thenThrow(new EntityValidateException(DRIVER_PASSWORD_IS_EMPTY));

        //then
        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_PASSWORD_IS_EMPTY));
    }

    @Test
    public void testEditDriverWhenRequestIsValidReturnIdOfUpdatedDriver() throws Exception {
        //given
        DriverDto driverDTO = getDriver();

        //when
        when(driversService.update(eq(DEFAULT_DRIVER_ID), eq(driverDTO), any(BindingResult.class)))
                .thenReturn(new DriverResponseDto(DEFAULT_DRIVER_ID));

        //then
        mockMvc.perform(patch("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_DRIVER_ID));
    }

    @Test
    public void testEditDriverWhenStatusIsInvalidReturnErrorResponse() throws Exception {
        //given
        DriverDto driverDTO = getRequestForUpdateDriverWithInvalidStatus();

        //when
        when(driversService.update(eq(DEFAULT_DRIVER_ID), eq(driverDTO), any(BindingResult.class)))
                .thenThrow(new EntityValidateException(INVALID_DRIVER_STATUS));

        //then
        mockMvc.perform(patch("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_DRIVER_STATUS));
    }

    @Test
    public void testDeactivateDriverWhenDriverExistsReturnIdOfDeactivatedDriver() throws Exception {
        //given

        //when
        when(driversService.deactivate(DEFAULT_DRIVER_ID)).thenReturn(new DriverResponseDto(DEFAULT_DRIVER_ID));

        //then
        mockMvc.perform(delete("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_DRIVER_ID));
    }

    @Test
    public void testDeactivateDriverWhenDriverWasNotFoundReturnErrorResponse() throws Exception {
        //given

        //when
        when(driversService.deactivate(DEFAULT_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, DEFAULT_DRIVER_ID)));

        //then
        mockMvc.perform(delete("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, DEFAULT_DRIVER_ID)));
    }
}
