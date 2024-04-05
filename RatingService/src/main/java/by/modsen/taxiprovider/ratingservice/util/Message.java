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
}
