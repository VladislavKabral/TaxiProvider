package by.modsen.taxiprovider.driverservice.integration.controller;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriversPageDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.mapper.DriverMapper;
import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.List;

import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@SqlGroup({
        @Sql(value = {"classpath:db/init-drivers-schema.sql"},
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = {"classpath:db/clean-changes-after-tests.sql"},
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class DriverControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private DriversRepository driversRepository;

    @Autowired
    private DriverMapper mapper;

    @Test
    public void testGetDriversWhenDriversExistReturnListOfDrivers() {
        List<Driver> drivers = driversRepository.findAll();
        DriverListDto expectedResponse = new DriverListDto(mapper.toListDto(drivers));

        DriverListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverListDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetDriverWhenDriverExistsReturnDriverWithGivenId() {
        Driver driver = driversRepository.findById(DEFAULT_DRIVER_ID).get();
        DriverDto expectedResponse = mapper.toDto(driver);

        DriverDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DEFAULT_DRIVER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetDriverWhenDriverWithGivenIdWasNotFoundReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTENT_DRIVER_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testGetDriversPageWhenPageAndSizeAreValidReturnDriversPage() {
        List<Driver> drivers = driversRepository
                .findAll(PageRequest.of(DEFAULT_PAGE - 1, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_FIELD)))
                .getContent();
        DriversPageDto expectedResponse = DriversPageDto.builder()
                .page(DEFAULT_PAGE)
                .size(DEFAULT_PAGE_SIZE)
                .content(mapper.toListDto(drivers))
                .build();

        DriversPageDto actualResponse = given()
                .port(port)
                .queryParam(PAGE_PARAM_NAME, DEFAULT_PAGE)
                .queryParam(SIZE_PARAM_NAME, DEFAULT_PAGE_SIZE)
                .queryParam(SORT_PARAM_NAME, DEFAULT_SORT_FIELD)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriversPageDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetDriversPageWhenThereAreNotAnyDriversOnThePageReturnEmptyList() {
        List<Driver> drivers = driversRepository
                .findAll(PageRequest.of(DEFAULT_INCORRECT_PAGE - 1, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_FIELD)))
                .getContent();

        DriversPageDto expectedResponse = DriversPageDto.builder()
                .page(DEFAULT_INCORRECT_PAGE)
                .size(DEFAULT_PAGE_SIZE)
                .content(mapper.toListDto(drivers))
                .build();

        DriversPageDto actualResponse = given()
                .port(port)
                .queryParam(PAGE_PARAM_NAME, DEFAULT_INCORRECT_PAGE)
                .queryParam(SIZE_PARAM_NAME, DEFAULT_PAGE_SIZE)
                .queryParam(SORT_PARAM_NAME, DEFAULT_SORT_FIELD)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriversPageDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetDriversPageWhenPageAndSizeAreInvalidReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(INVALID_PAGE_REQUEST)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .queryParam(PAGE_PARAM_NAME, DEFAULT_INVALID_PAGE)
                .queryParam(SIZE_PARAM_NAME, DEFAULT_INVALID_PAGE_SIZE)
                .queryParam(SORT_PARAM_NAME, DEFAULT_SORT_FIELD)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testGetFreeDriversWhenFreeDriversExistReturnListOfFreeDrivers() {
        DriverListDto expectedResponse = getDrivers();

        DriverListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_FREE_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverListDto.class);

        assertThat(actualResponse.getContent().size()).isEqualTo(expectedResponse.getContent().size());
        assertThat(actualResponse.getContent().get(0).getLastname())
                .isEqualTo(expectedResponse.getContent().get(0).getLastname());
        assertThat(actualResponse.getContent().get(1).getLastname())
                .isEqualTo(expectedResponse.getContent().get(1).getLastname());
        assertThat(actualResponse.getContent().get(2).getLastname())
                .isEqualTo(expectedResponse.getContent().get(2).getLastname());
    }

    @Test
    public void testGetDriverProfileWhenDriverExistsReturnDriverProfile() {

    }

    @Test
    public void testGetDriverProfileWhenDriversProfileWasNotFoundReturnErrorResponse() {

    }

    @Test
    public void testSaveDriverWhenRequestIsValidReturnIdOfCreatedDriver() {
        NewDriverDto request = getRequestForSaveDriver();
        DriverResponseDto expectedResponse = new DriverResponseDto(DEFAULT_INVALID_DRIVER_ID);

        DriverResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(DriverResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testSaveDriverWhenLastnameIsInvalidReturnErrorResponse() {

    }

    @Test
    public void testSaveDriverWhenFirstnameIsInvalidReturnErrorResponse() {

    }

    @Test
    public void testSaveDriverWhenEmailIsInvalidReturnErrorResponse() {

    }

    @Test
    public void testSaveDriverWhenPhoneNumberIsInvalidReturnErrorResponse() {

    }

    @Test
    public void testSaveDriverWhenPasswordIsInvalidReturnErrorResponse() {

    }

    @Test
    public void testEditDriverWhenRequestIsValidReturnIdOfUpdatedDriver() {

    }

    @Test
    public void testEditDriverWhenStatusIsInvalidReturnErrorResponse() {

    }

    @Test
    public void testDeactivateDriverWhenDriverExistsReturnIdOfDeactivatedDriver() {

    }

    @Test
    public void testDeactivateDriverWhenDriverWasNotFoundReturnErrorResponse() {

    }

}
