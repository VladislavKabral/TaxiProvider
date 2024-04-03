package by.modsen.taxiprovider.ratingservice.utility;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDTO;
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

    public TaxiUserRatingDTO getDriversRating() {
        return TaxiUserRatingDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(DEFAULT_RATING_VALUE)
                .build();
    }

    public TaxiUserRatingDTO getPassengerRating() {
        return TaxiUserRatingDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .value(DEFAULT_RATING_VALUE)
                .build();
    }

    public TaxiUserRequestDTO getRequestForDriverRating() {
        return TaxiUserRequestDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDTO getRequestForPassengerRating() {
        return TaxiUserRequestDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDTO getIncorrectRequestForDriverRating() {
        return TaxiUserRequestDTO.builder()
                .taxiUserId(INCORRECT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDTO getIncorrectRequestForPassengerRating() {
        return TaxiUserRequestDTO.builder()
                .taxiUserId(INCORRECT_TAXI_USER_ID)
                .role(PASSENGER_ROLE_NAME)
                .build();
    }

    public TaxiUserRequestDTO getIncorrectRequestForInitRating() {
        return TaxiUserRequestDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(INCORRECT_TAXI_USER_ROLE)
                .build();
    }

    public RatingDTO getRequestForRateDriver() {
        return RatingDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingDTO getInvalidRequestWithInvalidRoleForRateDriver() {
        return RatingDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(INCORRECT_TAXI_USER_ROLE)
                .value(NOT_DEFAULT_RATING_VALUE)
                .build();
    }

    public RatingDTO getInvalidRequestWithInvalidRatingValueForRateDriver() {
        return RatingDTO.builder()
                .taxiUserId(DEFAULT_TAXI_USER_ID)
                .role(DRIVER_ROLE_NAME)
                .value(NOT_DEFAULT_INVALID_RATING_VALUE)
                .build();
    }
}
