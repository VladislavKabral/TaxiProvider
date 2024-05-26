package by.modsen.taxiprovider.ratingservice.integration;

import by.modsen.taxiprovider.ratingservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.ratingservice.service.RatingsService;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static by.modsen.taxiprovider.ratingservice.utility.RatingsTestUtil.*;
import static by.modsen.taxiprovider.ratingservice.util.Message.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@SqlGroup({
        @Sql(value = {"classpath:db/init-ratings-schema.sql"},
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = {"classpath:db/clean-changes-after-tests.sql"},
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class RatingControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private RatingsService ratingsService;

    @Test
    public void testGetDriverRatingWhenDriverExistsReturnDriverRating() throws EntityNotFoundException {
        TaxiUserRequestDto request = getRequestForDriverRating();
        TaxiUserRatingDto expectedResponse = ratingsService.getTaxiUserRating(request);

        RatingResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_DRIVER_RATING_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RatingResponseDto.class);

        assertThat(actualResponse.getTaxiUserId()).isEqualTo(expectedResponse.getTaxiUserId());
        assertThat(actualResponse.getRole()).isEqualTo(expectedResponse.getRole());
        assertThat(actualResponse.getValue()).isEqualTo(expectedResponse.getValue());
    }

    @Test
    public void testGetPassengerRatingWhenPassengerExistsReturnPassengerRating() throws EntityNotFoundException {
        TaxiUserRequestDto request = getRequestForPassengerRating();
        TaxiUserRatingDto expectedResponse = ratingsService.getTaxiUserRating(request);

        RatingResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PASSENGER_RATING_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(RatingResponseDto.class);

        assertThat(actualResponse.getTaxiUserId()).isEqualTo(expectedResponse.getTaxiUserId());
        assertThat(actualResponse.getRole()).isEqualTo(expectedResponse.getRole());
        assertThat(actualResponse.getValue()).isEqualTo(expectedResponse.getValue());
    }

    @Test
    public void testGetDriverRatingWhenDriverDoesNotExistReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(TAXI_USER_NOT_FOUND,
                        DEFAULT_INVALID_TAXI_USER_ID,
                        DRIVER_ROLE_NAME))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTS_DRIVER_RATING_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testGetPassengerRatingWhenPassengerDoesNotExistReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(TAXI_USER_NOT_FOUND,
                        DEFAULT_INVALID_TAXI_USER_ID,
                        PASSENGER_ROLE_NAME))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTS_PASSENGER_RATING_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testInitDriverRatingWhenDriverRequestIsValidReturnRatingResponse() throws EntityValidateException {
        TaxiUserRequestDto request = getRequestForDriverRating();
        RatingResponseDto expectedResponse = ratingsService.initTaxiUserRatings(request);

        RatingResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(INIT_RATING_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(RatingResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testInitPassengerRatingWhenPassengerRequestIsValidReturnRatingResponse() throws EntityValidateException {
        TaxiUserRequestDto request = getRequestForPassengerRating();
        RatingResponseDto expectedResponse = ratingsService.initTaxiUserRatings(request);

        RatingResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(INIT_RATING_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(RatingResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testInitTaxiUserRatingWhenTaxiUserRoleIsInvalidReturnErrorResponse() {
        TaxiUserRequestDto request = getIncorrectRequestForInitRating();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(ROLE_IS_INVALID, request.getRole()))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(INIT_RATING_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testRateDriverWhenRateRequestIsValidReturnRatingResponse() throws EntityValidateException {
        RatingDto request = getRequestForRateDriver();

        RatingResponseDto expectedResponse = ratingsService.save(request);

        RatingResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(RATE_TAXI_USER_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(RatingResponseDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testRateDriverWhenRoleInRateRequestIsInvalidReturnErrorResponse() {
        RatingDto request = getInvalidRequestWithInvalidRoleForRateDriver();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(ROLE_IS_INVALID, request.getRole()))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(RATE_TAXI_USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testRateDriverWhenRatingValueInRateRequestIsInvalidReturnErrorResponse() {
        RatingDto request = getInvalidRequestWithInvalidRatingValueForRateDriver();

        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(RATING_MAXIMUM_VALUE_IS_INVALID)
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(request)
                .post(RATE_TAXI_USER_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }
}
