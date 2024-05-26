package by.modsen.taxiprovider.ridesservice.integration;

import by.modsen.taxiprovider.ridesservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodesListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static by.modsen.taxiprovider.ridesservice.utility.PromoCodesTestUtil.*;
import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@SqlGroup({
        @Sql(value = {"classpath:db/init-rides-schema.sql"},
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = {"classpath:db/clean-changes-after-tests.sql"},
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class PromoCodesControllerIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private PromoCodesService promoCodesService;

    @Test
    public void testGetPromoCodesWhenPromoCodesExistReturnListOfPromoCodes() {
        PromoCodesListDto expectedResponse = promoCodesService.findAll();

        PromoCodesListDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PROMO_CODES_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PromoCodesListDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPromoCodeByValueWhenPromoCodeExistsReturnPromoCode() throws EntityNotFoundException {
        PromoCodeDto expectedResponse = promoCodesService.findByValue(DEFAULT_PROMO_CODE_VALUE);

        PromoCodeDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_PROMO_CODE_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PromoCodeDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void testGetPromoCodeByValueWhenPromoCodeDoesNotExistReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, NON_EXISTS_PROMO_CODE_VALUE))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get(GET_NON_EXISTS_PROMO_CODE_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testSavePromoCodeWhenRequestIsValidReturnIdOfCreatedPromoCode() {
        PromoCodeDto request = getRequestForSavePromoCode();

        PromoCodeResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PROMO_CODES_PATH)
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(PromoCodeResponseDto.class);

        assertThat(actualResponse.getId()).isEqualTo(NON_EXISTS_PROMO_CODE_ID);
    }

    @Test
    public void testSavePromoCodeWhenRequestIsInvalidReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(PROMO_CODE_SIZE_IS_INVALID)
                .build();

        PromoCodeDto request = getInvalidRequestForSavePromoCode();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(GET_PROMO_CODES_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testEditPromoCodeWhenRequestIsValidReturnIdOfUpdatedPromoCode() {
        PromoCodeDto request = getRequestForEditPromoCode();

        PromoCodeResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_PROMO_CODE_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PromoCodeResponseDto.class);

        assertThat(actualResponse.getId()).isEqualTo(DEFAULT_PROMO_CODE_ID);
    }

    @Test
    public void testEditPromoCodeWhenRequestIsInvalidReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(DISCOUNT_IS_INVALID)
                .build();

        PromoCodeDto request = getInvalidRequestForEditPromoCode();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .patch(GET_PROMO_CODE_ID_PATH)
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }

    @Test
    public void testDeletePromoCodeWhenPromoCodeExistsReturnIdOfDeletedPromoCode() {
        PromoCodeResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(GET_PROMO_CODE_ID_PATH)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(PromoCodeResponseDto.class);

        assertThat(actualResponse.getId()).isEqualTo(DEFAULT_PROMO_CODE_ID);
    }

    @Test
    public void testDeletePromoCodeWhenPromoCodeDoesNotExistReturnErrorResponse() {
        ErrorResponseDto expectedResponse = ErrorResponseDto.builder()
                .message(String.format(PROMO_CODE_WITH_ID_NOT_FOUND, NON_EXISTS_PROMO_CODE_ID))
                .build();

        ErrorResponseDto actualResponse = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete(GET_NON_EXISTS_PROMO_CODE_ID_PATH)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract()
                .as(ErrorResponseDto.class);

        assertThat(actualResponse.getMessage()).isEqualTo(expectedResponse.getMessage());
    }
}
