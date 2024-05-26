package by.modsen.taxiprovider.passengerservice.integration;

import by.modsen.taxiprovider.passengerservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerListDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengersPageDto;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDto;
import by.modsen.taxiprovider.passengerservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
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

import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.utility.PassengersTestUtil.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.util.StreamUtils.copyToString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@SqlGroup({
        @Sql(value = {"classpath:db/init-passengers-schema.sql"},
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = {"classpath:db/clean-passengers-schema.sql"},
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PassengersControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private PassengersRepository passengersRepository;

    @Autowired
    private PassengerMapper passengerMapper;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void init() throws IOException {
        wireMockServer = new WireMockServer(wireMockConfig().port(8084));

        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching(INIT_PASSENGER_RATING_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        RatingResponseDto.class.getClassLoader().getResourceAsStream("response/passenger-rating.json"),
                                        Charset.defaultCharset()))));

        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(GET_PASSENGER_RATING_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        RatingResponseDto.class.getClassLoader().getResourceAsStream("response/passenger-rating.json"),
                                        Charset.defaultCharset()))));

        wireMockServer.start();
    }

    @AfterAll
    public static void stop() {
        wireMockServer.stop();
    }

    @Test
    public void testGetPassengersWhenPassengersExistReturnListOfPassengers() {
        List<Passenger> passengers = passengersRepository.findAll();
        PassengerListDto expectedResponse = new PassengerListDto(passengerMapper.toListDto(passengers));

        PassengerListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerListDto.class);

        assertThat(actualResponse.getContent().size()).isEqualTo(expectedResponse.getContent().size());
    }

    @Test
    public void testGetPassengerWhenPassengerExistsReturnPassengerWithGivenId() {
        Passenger passenger = passengersRepository.findById(DEFAULT_PASSENGER_ID).get();
        PassengerDto expectedResponse = passengerMapper.toDto(passenger);

        PassengerDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DEFAULT_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPassengerWhenPassengerWithGivenIdWasNotFoundReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(PASSENGER_NOT_FOUND, DEFAULT_INVALID_PASSENGER_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTENT_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testGetPassengersPageWhenPageAndSizeAreValidReturnPassengersPage() {
        List<Passenger> passengers = passengersRepository
                .findAll(PageRequest.of(DEFAULT_PAGE - 1, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_FIELD)))
                .getContent();
        PassengersPageDto expectedResponse = PassengersPageDto.builder()
                .page(DEFAULT_PAGE)
                .size(DEFAULT_PAGE_SIZE)
                .content(passengerMapper.toListDto(passengers))
                .build();

        PassengersPageDto actualResponse = given()
                .port(port)
                .queryParam(PAGE_PARAM_NAME, DEFAULT_PAGE)
                .queryParam(SIZE_PARAM_NAME, DEFAULT_PAGE_SIZE)
                .queryParam(SORT_PARAM_NAME, DEFAULT_SORT_FIELD)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengersPageDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPassengersPageWhenThereAreNotAnyPassengersOnThePageReturnEmptyList() {
        List<Passenger> drivers = passengersRepository
                .findAll(PageRequest.of(DEFAULT_INCORRECT_PAGE - 1, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_FIELD)))
                .getContent();

        PassengersPageDto expectedResponse = PassengersPageDto.builder()
                .page(DEFAULT_INCORRECT_PAGE)
                .size(DEFAULT_PAGE_SIZE)
                .content(passengerMapper.toListDto(drivers))
                .build();

        PassengersPageDto actualResponse = given()
                .port(port)
                .queryParam(PAGE_PARAM_NAME, DEFAULT_INCORRECT_PAGE)
                .queryParam(SIZE_PARAM_NAME, DEFAULT_PAGE_SIZE)
                .queryParam(SORT_PARAM_NAME, DEFAULT_SORT_FIELD)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengersPageDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPassengersPageWhenPageAndSizeAreInvalidReturnErrorResponse() {
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
                .get(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testGetPassengerProfileWhenPassengerExistsReturnPassengerProfile() {
        PassengerProfileDto expectedResponse = getPassengerProfile();

        PassengerProfileDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DEFAULT_PASSENGER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerProfileDto.class);

        assertThat(actualResponse.getPassenger().getLastname()).isEqualTo(expectedResponse.getPassenger().getLastname());
        assertThat(actualResponse.getPassenger().getFirstname()).isEqualTo(expectedResponse.getPassenger().getFirstname());
        assertThat(actualResponse.getRating()).isEqualTo(expectedResponse.getRating());
    }

    @Test
    public void testGetPassengerProfileWhenPassengersProfileWasNotFoundReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(PASSENGER_NOT_FOUND, DEFAULT_INVALID_PASSENGER_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DEFAULT_NON_EXISTS_PASSENGER_PROFILE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSavePassengerWhenRequestIsValidReturnIdOfCreatedPassenger() {
        NewPassengerDto request = getRequestForSavePassenger();
        PassengerResponseDto expectedResponse = new PassengerResponseDto(DEFAULT_INVALID_PASSENGER_ID);

        PassengerResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(PassengerResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testSavePassengerWhenLastnameIsInvalidReturnErrorResponse() {
        NewPassengerDto request = getRequestForSavePassengerWithInvalidLastName();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PASSENGER_LASTNAME_BODY_IS_INVALID)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSavePassengerWhenFirstnameIsInvalidReturnErrorResponse() {
        NewPassengerDto request = getRequestForSavePassengerWithInvalidFirstName();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PASSENGER_FIRSTNAME_BODY_IS_INVALID)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSavePassengerWhenEmailIsInvalidReturnErrorResponse() {
        NewPassengerDto request = getRequestForSavePassengerWithInvalidEmail();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PASSENGER_EMAIL_WRONG_FORMAT)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSavePassengerWhenPhoneNumberIsInvalidReturnErrorResponse() {
        NewPassengerDto request = getRequestForSavePassengerWithInvalidPhoneNumber();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSavePassengerWhenPasswordIsInvalidReturnErrorResponse() {
        NewPassengerDto request = getRequestForSavePassengerWithInvalidPassword();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PASSENGER_PASSWORD_IS_EMPTY)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PASSENGERS_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testEditPassengerWhenRequestIsValidReturnIdOfUpdatedPassenger() {
        PassengerDto request = getPassenger();

        PassengerResponseDto expectedResponse = new PassengerResponseDto(DEFAULT_PASSENGER_ID);

        PassengerResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_DEFAULT_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testEditPassengerWhenRequestIsInvalidReturnErrorResponse() {
        PassengerDto request = getRequestForEditPassengerWithInvalidPhoneNumber();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_DEFAULT_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testDeactivatePassengerWhenPassengerExistsReturnIdOfDeactivatedPassenger() {
        PassengerResponseDto expectedResponse = new PassengerResponseDto(DEFAULT_PASSENGER_ID);

        PassengerResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(DEACTIVATE_DEFAULT_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PassengerResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testDeactivatePassengerWhenPassengerWasNotFoundReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(PASSENGER_NOT_FOUND, DEFAULT_INVALID_PASSENGER_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(DEACTIVATE_NON_EXISTENT_PASSENGER_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }
}
