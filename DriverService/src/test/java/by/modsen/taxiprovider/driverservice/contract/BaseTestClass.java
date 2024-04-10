package by.modsen.taxiprovider.driverservice.contract;

import by.modsen.taxiprovider.driverservice.DriverServiceApplication;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.service.DriversService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import static by.modsen.taxiprovider.driverservice.util.Message.DRIVER_NOT_FOUND;
import static by.modsen.taxiprovider.driverservice.util.Message.INVALID_DRIVER_STATUS;
import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DriverServiceApplication.class)
public class BaseTestClass {

    @MockBean
    private DriversService driversService;

    @Autowired
    protected WebApplicationContext context;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        when(driversService.findAll()).thenReturn(getDrivers());

        when(driversService.findById(DEFAULT_DRIVER_ID)).thenReturn(getDriver());

        NewDriverDto newDriverRequest = getRequestForSaveDriver();
        when(driversService.save(newDriverRequest))
                .thenReturn(new DriverResponseDto(DEFAULT_INVALID_DRIVER_ID));

        DriverDto driverDTO = getDriver();
        when(driversService.update(DEFAULT_DRIVER_ID, driverDTO))
                .thenReturn(new DriverResponseDto(DEFAULT_DRIVER_ID));

        when(driversService.deactivate(DEFAULT_DRIVER_ID))
                .thenReturn(new DriverResponseDto(DEFAULT_DRIVER_ID));

        DriverProfileDto driverProfile = getDriverProfile();
        when(driversService.getDriverProfile(DEFAULT_DRIVER_ID)).thenReturn(driverProfile);

        DriverDto driverWithInvalidStatus = getRequestForUpdateDriverWithInvalidStatus();
        when(driversService.update(DEFAULT_DRIVER_ID, driverWithInvalidStatus))
                .thenThrow(new EntityValidateException(INVALID_DRIVER_STATUS));

        when(driversService.deactivate(DEFAULT_INVALID_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID)));

        when(driversService.getDriverProfile(DEFAULT_INVALID_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID)));

        when(driversService.findById(DEFAULT_INVALID_DRIVER_ID))
                .thenThrow(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID)));

        RestAssuredMockMvc.webAppContextSetup(this.context);
    }
}
