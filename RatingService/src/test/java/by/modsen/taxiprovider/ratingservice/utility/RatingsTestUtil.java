package by.modsen.taxiprovider.ratingservice.utility;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.ratingservice.model.Rating;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RatingsTestUtil {

    public final String GET_DRIVER_RATING_PATH = "/ratings?taxiUserId=1&role=DRIVER";
    public final String GET_NON_EXISTS_DRIVER_RATING_PATH = "/ratings?taxiUserId=4&role=DRIVER";
    public final String GET_PASSENGER_RATING_PATH = "/ratings?taxiUserId=1&role=PASSENGER";
    public final String GET_NON_EXISTS_PASSENGER_RATING_PATH = "/ratings?taxiUserId=4&role=PASSENGER";
    public final String INIT_RATING_PATH = "/ratings/init";
    public final String RATE_TAXI_USER_PATH = "/ratings";
    public final String TAXI_USER_ID_REQUEST_PARAM_NAME = "taxiUserId";
    public final String ROLE_REQUEST_PARAM_NAME = "role";
    public final long DEFAULT_TAXI_USER_ID = 1;
    public final long DEFAULT_INVALID_TAXI_USER_ID = 4;
    public final String DRIVER_ROLE_NAME = "DRIVER";
    public final String PASSENGER_ROLE_NAME = "PASSENGER";
    public final String INCORRECT_TAXI_USER_ROLE = "GJHfewew4";
    public final double DEFAULT_RATING_VALUE = 5.0;
    public final int NOT_DEFAULT_RATING_VALUE = 4;
    public final int NOT_DEFAULT_INVALID_RATING_VALUE = 9;
    public final int RATINGS_COUNT = 30;

    public TaxiUserRatingDto getDriversRating() {
        return TaxiUserRatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(DEFAULT_RATING_VALUE)
                .build();
    }

    public TaxiUserRatingDto getPassengerRating() {
        return TaxiUserRatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(DEFAULT_RATING_VALUE)
                .build();
    }

    public TaxiUserRequestDto getRequestForDriverRating() {
        return TaxiUserRequestDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDto getRequestForPassengerRating() {
        return TaxiUserRequestDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDto getIncorrectRequestForDriverRating() {
        return TaxiUserRequestDto.builder()
                .taxiUserId(DEFAULT_INVALID_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDto getIncorrectRequestForPassengerRating() {
        return TaxiUserRequestDto.builder()
                .taxiUserId(DEFAULT_INVALID_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDto getIncorrectRequestForInitRating() {
        return TaxiUserRequestDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(INCORRECT_TAXI_USER_ROLE)
                .build();
    }

    public RatingDto getRequestForRateDriver() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingDto getRequestForRatePassenger() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingDto getInvalidRequestWithInvalidRoleForRateDriver() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(INCORRECT_TAXI_USER_ROLE)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingDto getInvalidRequestWithInvalidRatingValueForRateDriver() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(NOT_DEFAULT_INVALID_RATING_VALUE)
                .build();
    }

    public RatingDto getInvalidRequestWithInvalidRatingValueForRatePassenger() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(NOT_DEFAULT_INVALID_RATING_VALUE)
                .build();
    }

    public List<Rating> getDriverRatings(){
        List<Rating> ratings = new ArrayList<>();
        for (int i = 0; i < RATINGS_COUNT; i++) {
            ratings.add(Rating.builder()
                            .taxiUserId(DEFAULT_TAXI_USER_ID)
                            .role(DRIVER_ROLE_NAME)
                            .value((int) DEFAULT_RATING_VALUE)
                            .createdAt(LocalDateTime.now())
                    .build());
        }
        return ratings;
    }

    public List<Rating> getPassengerRatings(){
        List<Rating> ratings = new ArrayList<>();
        for (int i = 0; i < RATINGS_COUNT; i++) {
            ratings.add(Rating.builder()
                            .taxiUserId(DEFAULT_TAXI_USER_ID)
                            .role(PASSENGER_ROLE_NAME)
                            .value((int) DEFAULT_RATING_VALUE)
                            .createdAt(LocalDateTime.now())
                    .build());
        }
        return ratings;
    }

    public List<Rating> getEmptyListOfRatings(){
        return new ArrayList<>();
    }

    public Rating getDriverRating() {
        return Rating.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Rating getPassengersRating() {
        return Rating.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public RatingResponseDto getDriverRatingResponse() {
        return RatingResponseDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingResponseDto getPassengerRatingResponse() {
        return RatingResponseDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingResponseDto getNotDefaultDriverRatingResponse() {
        return RatingResponseDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingResponseDto getNotDefaultPassengerRatingResponse() {
        return RatingResponseDto.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }
}
