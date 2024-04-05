package by.modsen.taxiprovider.ratingservice.utility;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RatingsTestUtil {

    public final String TAXI_USER_ID_REQUEST_PARAM_NAME = "taxiUserId";
    public final String ROLE_REQUEST_PARAM_NAME = "role";
    public final long DEFAULT_TAXI_USER_ID = 1;
    public final long INCORRECT_TAXI_USER_ID = 144;
    public final String DRIVER_ROLE_NAME = "DRIVER";
    public final String PASSENGER_ROLE_NAME = "PASSENGER";
    public final String INCORRECT_TAXI_USER_ROLE = "GJHfewew4";
    public final double DEFAULT_RATING_VALUE = 5.0;
    public final int NOT_DEFAULT_RATING_VALUE = 4;
    public final int NOT_DEFAULT_INVALID_RATING_VALUE = 9;

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
                .taxiUserId(INCORRECT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDto getIncorrectRequestForPassengerRating() {
        return TaxiUserRequestDto.builder()
                .taxiUserId(INCORRECT_TAXI_USER_ID)
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
}
