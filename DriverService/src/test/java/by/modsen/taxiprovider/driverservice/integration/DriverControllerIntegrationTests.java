package by.modsen.taxiprovider.driverservice.integration;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriversPageDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.driverservice.mapper.DriverMapper;
import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@SqlGroup({
        @Sql(value = {"classpath:db/init-drivers-schema.sql"},
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
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

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void init() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().port(8084));

        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching(INIT_DRIVER_RATING_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        RatingResponseDto.class.getClassLoader().getResourceAsStream("response/driver-rating.json"),
                                        Charset.defaultCharset()))));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(GET_DRIVER_RATING_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        RatingResponseDto.class.getClassLoader().getResourceAsStream("response/driver-rating.json"),
                                        Charset.defaultCharset()))));

        wireMockServer.start();
    }

    @AfterAll
    public static void stop() {
        wireMockServer.stop();
    }

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
        DriverProfileDto expectedResponse = getDriverProfile();

        DriverProfileDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DEFAULT_DRIVER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverProfileDto.class);

        assertThat(actualResponse.getDriver().getLastname()).isEqualTo(expectedResponse.getDriver().getLastname());
        assertThat(actualResponse.getDriver().getFirstname()).isEqualTo(expectedResponse.getDriver().getFirstname());
        assertThat(actualResponse.getRating()).isEqualTo(expectedResponse.getRating());
    }

    @Test
    public void testGetDriverProfileWhenDriversProfileWasNotFoundReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DEFAULT_NON_EXISTS_DRIVER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
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
        NewDriverDto request = getRequestForSaveDriverWithInvalidLastName();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DRIVER_LASTNAME_BODY_IS_INVALID)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSaveDriverWhenFirstnameIsInvalidReturnErrorResponse() {
        NewDriverDto request = getRequestForSaveDriverWithInvalidFirstName();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DRIVER_FIRSTNAME_BODY_IS_INVALID)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSaveDriverWhenEmailIsInvalidReturnErrorResponse() {
        NewDriverDto request = getRequestForSaveDriverWithInvalidEmail();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DRIVER_EMAIL_WRONG_FORMAT)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSaveDriverWhenPhoneNumberIsInvalidReturnErrorResponse() {
        NewDriverDto request = getRequestForSaveDriverWithInvalidPhoneNumber();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSaveDriverWhenPasswordIsInvalidReturnErrorResponse() {
        NewDriverDto request = getRequestForSaveDriverWithInvalidPassword();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DRIVER_PASSWORD_IS_EMPTY)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_DRIVERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testEditDriverWhenRequestIsValidReturnIdOfUpdatedDriver() {
        DriverDto request = getDriver();

        DriverResponseDto expectedResponse = new DriverResponseDto(DEFAULT_DRIVER_ID);

        DriverResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_DEFAULT_DRIVER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testEditDriverWhenStatusIsInvalidReturnErrorResponse() {
        DriverDto request = getRequestForUpdateDriverWithInvalidStatus();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(INVALID_DRIVER_STATUS)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_DEFAULT_DRIVER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testDeactivateDriverWhenDriverExistsReturnIdOfDeactivatedDriver() {
        DriverResponseDto expectedResponse = new DriverResponseDto(DEFAULT_DRIVER_ID);

        DriverResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(DEACTIVATE_DEFAULT_DRIVER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DriverResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testDeactivateDriverWhenDriverWasNotFoundReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(DRIVER_NOT_FOUND, DEFAULT_INVALID_DRIVER_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(DEACTIVATE_NON_EXISTENT_DRIVER_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }
}
