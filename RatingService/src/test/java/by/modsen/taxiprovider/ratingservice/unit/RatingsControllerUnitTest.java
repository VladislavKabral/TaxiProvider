package by.modsen.taxiprovider.ratingservice.unit;

import by.modsen.taxiprovider.ratingservice.controller.rating.RatingsController;
import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.ratingservice.service.RatingsService;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static by.modsen.taxiprovider.ratingservice.util.Message.RATING_MAXIMUM_VALUE_IS_INVALID;
import static by.modsen.taxiprovider.ratingservice.util.Message.ROLE_IS_INVALID;
import static by.modsen.taxiprovider.ratingservice.util.Message.TAXI_USER_NOT_FOUND;
import static by.modsen.taxiprovider.ratingservice.utility.RatingsTestUtil.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(RatingsController.class)
public class RatingsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingsService ratingsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetDriverRatingWhenDriverExistsReturnDriverRating() throws Exception {
        //given
        TaxiUserRequestDto request = getRequestForDriverRating();
        TaxiUserRatingDto rating = getDriversRating();

        //when
        when(ratingsService.getTaxiUserRating(request)).thenReturn(rating);

        //then
        mockMvc.perform(get("/ratings")
                        .queryParam(TAXI_USER_ID_REQUEST_PARAM_NAME, String.valueOf(request.getTaxiUserId()))
                        .queryParam(ROLE_REQUEST_PARAM_NAME, request.getRole())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxiUserId").value(rating.getTaxiUserId()))
                .andExpect(jsonPath("$.role").value(rating.getRole()))
                .andExpect(jsonPath("$.value").value(rating.getValue()));
    }

    @Test
    public void testGetPassengerRatingWhenPassengerExistsReturnPassengerRating() throws Exception {
        //given
        TaxiUserRequestDto request = getRequestForPassengerRating();
        TaxiUserRatingDto rating = getPassengerRating();

        //when
        when(ratingsService.getTaxiUserRating(request)).thenReturn(rating);

        //then
        mockMvc.perform(get("/ratings")
                        .queryParam(TAXI_USER_ID_REQUEST_PARAM_NAME, String.valueOf(request.getTaxiUserId()))
                        .queryParam(ROLE_REQUEST_PARAM_NAME, request.getRole())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxiUserId").value(rating.getTaxiUserId()))
                .andExpect(jsonPath("$.role").value(rating.getRole()))
                .andExpect(jsonPath("$.value").value(rating.getValue()));
    }

    @Test
    public void testGetDriverRatingWhenDriverDoesNotExistReturnErrorResponse() throws Exception {
        //given
        TaxiUserRequestDto request = getIncorrectRequestForDriverRating();

        //when
        when(ratingsService.getTaxiUserRating(request)).thenThrow(new EntityNotFoundException(String.format(TAXI_USER_NOT_FOUND,
                    request.getTaxiUserId(),
                    request.getRole())));

        //then
        mockMvc.perform(get("/ratings")
                        .queryParam(TAXI_USER_ID_REQUEST_PARAM_NAME, String.valueOf(request.getTaxiUserId()))
                        .queryParam(ROLE_REQUEST_PARAM_NAME, request.getRole())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(TAXI_USER_NOT_FOUND,
                        request.getTaxiUserId(),
                        request.getRole())));
    }

    @Test
    public void testGetPassengerRatingWhenPassengerDoesNotExistReturnErrorResponse() throws Exception {
        //given
        TaxiUserRequestDto request = getIncorrectRequestForPassengerRating();

        //when
        when(ratingsService.getTaxiUserRating(request)).thenThrow(new EntityNotFoundException(String.format(TAXI_USER_NOT_FOUND,
                request.getTaxiUserId(),
                request.getRole())));

        //then
        mockMvc.perform(get("/ratings")
                        .queryParam(TAXI_USER_ID_REQUEST_PARAM_NAME, String.valueOf(request.getTaxiUserId()))
                        .queryParam(ROLE_REQUEST_PARAM_NAME, request.getRole())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format(TAXI_USER_NOT_FOUND,
                        request.getTaxiUserId(),
                        request.getRole())));
    }

    @Test
    public void testInitDriverRatingWhenDriverRequestIsValidReturnRatingResponse() throws Exception {
        //given
        TaxiUserRequestDto request = getRequestForDriverRating();

        //when
        when(ratingsService.initTaxiUserRatings(request))
                .thenReturn(RatingResponseDto.builder()
                        .taxiUserId(request.getTaxiUserId())
                        .role(request.getRole())
                        .value(DEFAULT_RATING_VALUE)
                        .build());

        //then
        mockMvc.perform(post("/ratings/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taxiUserId").value(request.getTaxiUserId()))
                .andExpect(jsonPath("$.role").value(request.getRole()))
                .andExpect(jsonPath("$.value").value(DEFAULT_RATING_VALUE));
    }

    @Test
    public void testInitPassengerRatingWhenPassengerRequestIsValidReturnRatingResponse() throws Exception {
        //given
        TaxiUserRequestDto request = getRequestForPassengerRating();

        //when
        when(ratingsService.initTaxiUserRatings(request))
                .thenReturn(RatingResponseDto.builder()
                        .taxiUserId(request.getTaxiUserId())
                        .role(request.getRole())
                        .value(DEFAULT_RATING_VALUE)
                        .build());

        //then
        mockMvc.perform(post("/ratings/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taxiUserId").value(request.getTaxiUserId()))
                .andExpect(jsonPath("$.role").value(request.getRole()))
                .andExpect(jsonPath("$.value").value(DEFAULT_RATING_VALUE));
    }

    @Test
    public void testInitTaxiUserRatingWhenTaxiUserRoleIsInvalidReturnErrorResponse() throws Exception {
        //given
        TaxiUserRequestDto request = getIncorrectRequestForInitRating();

        //when
        when(ratingsService.initTaxiUserRatings(request))
                .thenThrow(new EntityValidateException(String.format(ROLE_IS_INVALID, request.getRole())));

        //then
        mockMvc.perform(post("/ratings/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format(ROLE_IS_INVALID, request.getRole())));
    }

    @Test
    public void testRateDriverWhenRateRequestIsValidReturnRatingResponse() throws Exception {
        //given
        RatingDto request = getRequestForRateDriver();

        //when
        when(ratingsService.save(request))
                .thenReturn(RatingResponseDto.builder()
                        .taxiUserId(request.getTaxiUserId())
                        .role(request.getRole())
                        .value(request.getValue())
                        .build());

        //then
        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.taxiUserId").value(request.getTaxiUserId()))
                .andExpect(jsonPath("$.role").value(request.getRole()))
                .andExpect(jsonPath("$.value").value(request.getValue()));
    }

    @Test
    public void testRateDriverWhenRoleInRateRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        RatingDto request = getInvalidRequestWithInvalidRoleForRateDriver();

        //when
        when(ratingsService.save(request))
                .thenThrow(new EntityValidateException(String.format(ROLE_IS_INVALID, request.getRole())));

        //then
        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(String.format(ROLE_IS_INVALID, request.getRole())));
    }

    @Test
    public void testRateDriverWhenRatingValueInRateRequestIsInvalidReturnErrorResponse() throws Exception {
        //given
        RatingDto request = getInvalidRequestWithInvalidRatingValueForRateDriver();

        //when
        when(ratingsService.save(request))
                .thenThrow(new EntityValidateException(RATING_MAXIMUM_VALUE_IS_INVALID));

        //then
        mockMvc.perform(post("/ratings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(RATING_MAXIMUM_VALUE_IS_INVALID));
    }
}
