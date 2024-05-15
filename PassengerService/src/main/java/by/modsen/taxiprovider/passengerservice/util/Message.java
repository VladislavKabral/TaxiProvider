package by.modsen.taxiprovider.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {

    public final String INVALID_PAGE_REQUEST = "Number of page and count of elements can't be less than zero.";
    public final String PASSENGER_NOT_FOUND = "Passenger with id '%d' wasn't found.";
    public final String PASSENGER_NOT_CREATED = "Passenger with email '%s' wasn't created.";
    public final String PASSENGER_ID_IS_NULL = "Passenger's id must be not null.";
    public final String PASSENGER_ID_MINIMAL_VALUE_IS_INVALID = "Minimal value of passenger's id is '1'.";
    public final String PASSENGER_ROLE_IS_EMPTY = "Passenger's role must be not empty.";
    public final String PASSENGER_LASTNAME_SIZE_IS_INVALID = "Passenger's lastname must be between 2 and 50 symbols.";
    public final String PASSENGER_LASTNAME_BODY_IS_INVALID = "Passenger's lastname must contain only letters.";
    public final String PASSENGER_LASTNAME_IS_EMPTY = "Passenger's lastname must be not empty.";
    public final String PASSENGER_FIRSTNAME_SIZE_IS_INVALID = "Passenger's firstname must be between 2 and 50 symbols.";
    public final String PASSENGER_FIRSTNAME_BODY_IS_INVALID = "Passenger's firstname must contain only letters.";
    public final String PASSENGER_FIRSTNAME_IS_EMPTY = "Passenger's firstname must be not empty.";
    public final String PASSENGER_EMAIL_WRONG_FORMAT = "Wrong email format.";
    public final String PASSENGER_EMAIL_IS_EMPTY = "Passenger's email must be not empty.";
    public final String PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG = "Wrong phone number format.";
    public final String PASSENGER_PHONE_NUMBER_IS_EMPTY = "Passenger's phone number must be not empty.";
    public final String PASSENGER_PASSWORD_IS_EMPTY = "Passenger's password must be not empty.";
    public final String EXTERNAL_SERVICE_IS_UNAVAILABLE = "Cannot connect to rating's service.";
    public final String EXTERNAL_SERVICE_ERROR = "Cannot get response from the rating's service.";
    public final String REQUEST_PARAMETER_IS_INVALID = "Failed to convert value in request parameter.";
    public final String METHOD_NOT_ALLOWED = "%s for this endpoint.";
    public final String CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE = "External service '%s' failed to process after max retries.";
    public final String PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS = "A passenger with email '%s' already exists.";
    public final String PASSENGER_WITH_GIVEN_PHONE_NUMBER_ALREADY_EXISTS = "A passenger with phone number '%s' already exists.";
    public final String FIND_ALL_PASSENGERS = "Finding all passengers.";
    public final String RECEIVED_PAGE_PARAMETERS_ARE_INVALID = "Invalid page parameters were received";
    public final String FIND_PASSENGERS = "Finding passengers from the given page";
    public final String FIND_PASSENGER_BY_ID = "Finding the passenger with id '%d'";
    public final String SAVE_NEW_PASSENGER = "Saving a new passenger";
    public final String PASSENGER_WAS_SAVED = "The passenger '%s' '%s' was saved.";
    public final String UPDATE_PASSENGER = "Updating the passenger with id '%d'";
    public final String PASSENGER_WAS_UPDATED = "The passenger with id '%d' was updated.";
    public final String DEACTIVATE_PASSENGER = "Deactivating the passenger with id '%d'";
    public final String PASSENGER_WAS_DEACTIVATED = "The passenger with id '%d' was deactivated.";
    public final String FIND_PASSENGER_PROFILE = "Finding a profile of the passenger with id '%d'";
    public final String CREATING_PASSENGER_ALREADY_EXISTS = "Creating passenger already exists (existing parameter is '%s')";
}