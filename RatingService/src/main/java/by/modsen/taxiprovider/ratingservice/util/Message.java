package by.modsen.taxiprovider.ratingservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {
    public final String RATING_MINIMAL_VALUE_IS_INVALID = "Minimal value for the rating's value is '1'.";
    public final String RATING_MAXIMUM_VALUE_IS_INVALID = "Maximum value for the rating's value is '5'.";
    public final String TAXI_USER_ID_IS_NULL = "Taxi user's id must be not null.";
    public final String TAXI_USER_ID_MINIMAL_VALUE = "Minimal value for taxi user's id id '1'.";
    public final String TAXI_USER_ROLE_IS_EMPTY = "Taxi user's role must be not empty.";
    public final String RATING_VALUE_IS_EMPTY = "Value of rating must be not empty.";
    public final String TAXI_USER_NOT_FOUND = "Cannot find ratings of user with id '%d' and role '%s'.";
    public final String REQUEST_PARAM_IS_INVALID = "Failed to convert value in request parameter.";
    public final String METHOD_NOT_ALLOWED = "%s for this endpoint.";
    public final String ROLE_IS_INVALID = "%s is wrong role name.";
    public final String GETTING_RATING = "Getting rating of the taxi user with id '%d' and role '%s'.";
    public final String RATING_WAS_FOUND = "The rating is '%f'";
    public final String INITIALIZATION_TAXI_USER_RATING = "Initializing rating of the taxi user with id '%d' and role '%s'.";
    public final String RATING_WAS_INITIALIZED = "The rating was initialized";
    public final String RATING_TAXI_USER = "Rating the taxi user with id '%d' and role '%s' and value '%d'.";
    public final String TAXI_USER_WAS_RATED = "The taxi user was rated.";
}
