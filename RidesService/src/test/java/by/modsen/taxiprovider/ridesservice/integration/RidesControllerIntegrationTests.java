package by.modsen.taxiprovider.ridesservice.integration;

import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.ridesservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideListDto;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

import static by.modsen.taxiprovider.ridesservice.util.Message.DESTINATION_ADDRESS_IS_EMPTY;
import static by.modsen.taxiprovider.ridesservice.util.Message.RIDE_NOT_FOUND;
import static by.modsen.taxiprovider.ridesservice.util.Message.SOURCE_ADDRESS_IS_EMPTY;
import static by.modsen.taxiprovider.ridesservice.utility.RidesTestUtil.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@SqlGroup({
        @Sql(value = {"classpath:db/init-rides-schema.sql"},
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = {"classpath:db/clean-changes-after-tests.sql"},
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class RidesControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private RidesService ridesService;

    private static WireMockServer driversMockServer;

    private static WireMockServer paymentsMockServer;

    private static WireMockServer distanceMockServer;

    @BeforeAll
    public static void init() throws IOException {
        driversMockServer = new WireMockServer(wireMockConfig().port(8082));
        paymentsMockServer = new WireMockServer(wireMockConfig().port(8084));
        distanceMockServer = new WireMockServer(wireMockConfig().port(8083));

        driversMockServer.stubFor(WireMock.get(WireMock.urlMatching(GET_FREE_DRIVERS_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        DriverListDto.class.getClassLoader().getResourceAsStream("response/free-drivers.json"),
                                        Charset.defaultCharset()))));

        driversMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(GET_DRIVER_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        DriverDto.class.getClassLoader().getResourceAsStream("response/driver.json"),
                                        Charset.defaultCharset()))));

        driversMockServer.stubFor(WireMock.patch(WireMock.urlEqualTo(GET_DRIVER_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        DriverDto.class.getClassLoader().getResourceAsStream("response/updated-driver.json"),
                                        Charset.defaultCharset()))));

        paymentsMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(PAYMENT_RIDE_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)));

        distanceMockServer.stubFor(WireMock.post(WireMock.urlEqualTo(DISTANCE_CALCULATION_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(copyToString(
                                HttpResponse.class.getClassLoader().getResourceAsStream("response/distance.json"),
                                Charset.defaultCharset()))));

        driversMockServer.start();
        paymentsMockServer.start();
        distanceMockServer.start();
    }

    @AfterAll
    public static void stop() {
        driversMockServer.stop();
        paymentsMockServer.stop();
        distanceMockServer.stop();
    }

    @Test
    public void testGetRidesWhenRidesExistReturnListOfRides() {
        RideListDto expectedResponse = ridesService.findAll();

        RideListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideListDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPassengerRidesWhenRidesExistReturnListOfRides() {
        RideListDto expectedResponse = ridesService.findByPassengerId(DEFAULT_PASSENGER_ID);

        RideListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PASSENGER_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideListDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPassengerRidesWhenRidesDoNotExistReturnErrorResponse() {
        RideListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTS_PASSENGER_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideListDto.class);

        assertThat(actualResponse.getContent().size()).isEqualTo(0);
    }

    @Test
    public void testGetDriverRidesWhenRidesExistReturnListOfRides() {
        RideListDto expectedResponse = ridesService.findByDriverId(DEFAULT_DRIVER_ID);

        RideListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DRIVER_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideListDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetDriverRidesWhenRidesDoNotExistReturnErrorResponse() {
        RideListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTS_DRIVER_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideListDto.class);

        assertThat(actualResponse.getContent().size()).isEqualTo(0);
    }

    @Test
    public void testGetRideByIdWhenRideExistsReturnRide() throws EntityNotFoundException {
        RideDto expectedResponse = ridesService.findById(DEFAULT_RIDE_ID);

        RideDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetRideByIdWhenRideDoesNotExistReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(RIDE_NOT_FOUND, DEFAULT_NON_EXISTS_RIDE_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTS_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSaveRideWhenRequestIsValidReturnIdOfCreatedRide() {
        NewRideDto request = getRequestForSaveRide();

        RideResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(RideResponseDto.class);

        assertThat(actualResponse.getId()).isEqualTo(DEFAULT_NON_EXISTS_RIDE_ID);
    }

    @Test
    public void testSaveRideWhenRequestIsInvalidReturnErrorResponse() {
        NewRideDto request = getInvalidRequestForSaveRide();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(SOURCE_ADDRESS_IS_EMPTY)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testEditRideWhenRequestIsValidReturnIdOfUpdatedRide() {
        RideResponseDto expectedResponse = getResponse();

        RideDto request = getRequestForEditDrive();

        RideResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testEditRideWhenRequestIsInvalidReturnErrorResponse() {
        RideDto request = getInvalidRequestForEditDrive();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DESTINATION_ADDRESS_IS_EMPTY)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_RIDES_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testCancelRideWhenRideExistsReturnIdOfCancelledRide() {
        RideResponseDto expectedResponse = getResponse();

        RideResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(GET_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RideResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testCancelRideWhenRideDoesNotExistReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(RIDE_NOT_FOUND, DEFAULT_NON_EXISTS_RIDE_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(GET_NON_EXISTS_RIDE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }
}
