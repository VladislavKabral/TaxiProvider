package by.modsen.taxiprovider.driverservice.integration;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static by.modsen.taxiprovider.driverservice.util.Message.*;
import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"classpath:clean-changes-after-tests.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class DriverControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetDriversWhenDriversExistReturnListOfDrivers() throws Exception {
        List<DriverDTO> drivers = getDrivers();

        mockMvc.perform(get("/drivers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].lastname").value(drivers.get(0).getLastname()))
                .andExpect(jsonPath("$[1].lastname").value(drivers.get(1).getLastname()))
                .andExpect(jsonPath("$[2].lastname").value(drivers.get(2).getLastname()));
    }

    @Test
    public void testGetSortedDriversByLastnameWhenDriversExistReturnListOfDrivers() throws Exception {
        List<DriverDTO> drivers = getSortedDrivers();

        mockMvc.perform(get("/drivers")
                        .queryParam("sort", DEFAULT_SORT_FIELD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].lastname").value(drivers.get(0).getLastname()))
                .andExpect(jsonPath("$[1].lastname").value(drivers.get(1).getLastname()))
                .andExpect(jsonPath("$[2].lastname").value(drivers.get(2).getLastname()));
    }

    @Test
    public void testGetDriverWhenDriverExistsReturnDriverWithGivenId() throws Exception {
        DriverDTO driver = getDriver();

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
        mockMvc.perform(get("/drivers/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID)));
    }

    @Test
    public void testGetDriversPageWhenPageAndSizeAreValidReturnDriversPage() throws Exception {
        Page<DriverDTO> drivers = new PageImpl<>(getSortedDrivers());

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
        mockMvc.perform(get("/drivers")
                        .queryParam("page", String.valueOf(DEFAULT_INCORRECT_PAGE))
                        .queryParam("size", String.valueOf(DEFAULT_PAGE_SIZE))
                        .queryParam("sort", DEFAULT_SORT_FIELD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(DRIVERS_ON_PAGE_NOT_FOUND));
    }

    @Test
    public void testGetDriversPageWhenPageAndSizeAreInvalidReturnErrorResponse() throws Exception {
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
        List<DriverDTO> drivers = getDrivers();

        mockMvc.perform(get("/drivers/free")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].lastname").value(drivers.get(0).getLastname()))
                .andExpect(jsonPath("$[1].lastname").value(drivers.get(1).getLastname()))
                .andExpect(jsonPath("$[2].lastname").value(drivers.get(2).getLastname()));
    }

    @Test
    public void testGetDriverProfileWhenDriverExistsReturnDriverProfile() throws Exception {
        //given
        DriverProfileDTO driverProfile = getDriverProfile();

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
        mockMvc.perform(get("/drivers/4/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID)));
    }

    @Test
    public void testSaveDriverWhenRequestIsValidReturnIdOfCreatedDriver() throws Exception {
        NewDriverDTO newDriverRequest = getRequestForSaveDriver();

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(DEFAULT_INVALID_DRIVER_ID + ". "));
    }

    @Test
    public void testSaveDriverWhenLastnameIsInvalidReturnErrorResponse() throws Exception {
        NewDriverDTO newDriverRequest = getRequestForSaveDriverWithInvalidLastName();

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_LASTNAME_BODY_IS_INVALID + ". "));
    }

    @Test
    public void testSaveDriverWhenFirstnameIsInvalidReturnErrorResponse() throws Exception {
        NewDriverDTO newDriverRequest = getRequestForSaveDriverWithInvalidFirstName();

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_FIRSTNAME_BODY_IS_INVALID + ". "));
    }

    @Test
    public void testSaveDriverWhenEmailIsInvalidReturnErrorResponse() throws Exception {
        NewDriverDTO newDriverRequest = getRequestForSaveDriverWithInvalidEmail();

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_EMAIL_WRONG_FORMAT + ". "));
    }

    @Test
    public void testSaveDriverWhenPhoneNumberIsInvalidReturnErrorResponse() throws Exception {
        NewDriverDTO newDriverRequest = getRequestForSaveDriverWithInvalidPhoneNumber();

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG + ". "));
    }

    @Test
    public void testSaveDriverWhenPasswordIsInvalidReturnErrorResponse() throws Exception {
        NewDriverDTO newDriverRequest = getRequestForSaveDriverWithInvalidPassword();

        mockMvc.perform(post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newDriverRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DRIVER_PASSWORD_IS_EMPTY + ". "));
    }

    @Test
    public void testEditDriverWhenRequestIsValidReturnIdOfUpdatedDriver() throws Exception {
        DriverDTO driverDTO = getDriver();

        mockMvc.perform(patch("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_DRIVER_ID));
    }

    @Test
    public void testEditDriverWhenStatusIsInvalidReturnErrorResponse() throws Exception {
        DriverDTO driverDTO = getRequestForUpdateDriverWithInvalidStatus();

        mockMvc.perform(patch("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(INVALID_DRIVER_STATUS));
    }

    @Test
    public void testDeactivateDriverWhenDriverExistsReturnIdOfDeactivatedDriver() throws Exception {
        mockMvc.perform(delete("/drivers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(DEFAULT_DRIVER_ID));
    }

    @Test
    public void testDeactivateDriverWhenDriverWasNotFoundReturnErrorResponse() throws Exception {
        mockMvc.perform(delete("/drivers/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID)));
    }

}
