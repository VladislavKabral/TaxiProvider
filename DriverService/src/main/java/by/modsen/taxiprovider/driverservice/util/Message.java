package by.modsen.taxiprovider.driverservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {

    public final String INVALID_PAGE_REQUEST = "Number of page and count of elements can't be less than zero.";
    public final String DRIVER_NOT_FOUND = "Driver with id '%d' wasn't found.";
    public final String DRIVERS_NOT_CREATED = "Driver with email '%s' wasn't created.";
    public final String INVALID_DRIVER_STATUS = "Invalid ride status for driver.";
    public final String DRIVER_ID_IS_NULL = "Driver's id must be not null.";
    public final String DRIVER_ID_MINIMAL_VALUE_IS_INVALID = "Minimal value of driver's id is '1'.";
    public final String DRIVER_ROLE_IS_EMPTY = "Driver's role must be not empty.";
    public final String DRIVER_LASTNAME_SIZE_IS_INVALID = "Driver's lastname must be between 2 and 50 symbols.";
    public final String DRIVER_LASTNAME_BODY_IS_INVALID = "Driver's lastname must contain only letters.";
    public final String DRIVER_LASTNAME_IS_EMPTY = "Driver's lastname must be not empty.";
    public final String DRIVER_FIRSTNAME_SIZE_IS_INVALID = "Driver's firstname must be between 2 and 50 symbols.";
    public final String DRIVER_FIRSTNAME_BODY_IS_INVALID = "Driver's firstname must contain only letters.";
    public final String DRIVER_FIRSTNAME_IS_EMPTY = "Driver's firstname must be not empty.";
    public final String DRIVER_EMAIL_WRONG_FORMAT = "Wrong email format.";
    public final String DRIVER_EMAIL_IS_EMPTY = "Driver's email must be not empty.";
    public final String DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG = "Wrong phone number format.";
    public final String DRIVER_PHONE_NUMBER_IS_EMPTY = "Driver's phone number must be not empty.";
    public final String DRIVER_PASSWORD_IS_EMPTY = "Driver's password must be not empty.";
    public final String EXTERNAL_SERVICE_ERROR = "Cannot get response from the rating's service.";
    public final String METHOD_NOT_ALLOWED = "%s for this endpoint.";
    public final String REQUEST_PARAMETER_IS_INVALID = "Failed to convert value in request parameter.";
    public final String EXTERNAL_SERVICE_IS_UNAVAILABLE = "Cannot connect to rating's service.";
    public final String CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE = "External service '%s' failed to process after max retries.";
    public final String DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS = "Driver with email '%s' already exists.";
    public final String DRIVER_WITH_GIVEN_PHONE_NUMBER_ALREADY_EXISTS = "Driver with phone number '%s' already exists.";
    public final String FIND_ALL_DRIVERS = "Finding all drivers.";
    public final String RECEIVED_PAGE_PARAMETERS_ARE_INVALID = "Invalid page parameters were received";
    public final String FIND_DRIVERS = "Finding drivers from the given page";
    public final String FIND_DRIVER_BY_ID = "Finding the driver with id '%d'";
    public final String FIND_FREE_DRIVERS = "Finding free drivers";
    public final String SAVE_NEW_DRIVER = "Saving a new driver";
    public final String DRIVER_WAS_SAVED = "The driver '%s' '%s' was saved.";
    public final String UPDATE_DRIVER = "Updating the driver with id '%d'";
    public final String DRIVER_WAS_UPDATED = "The driver with id '%d' was updated.";
    public final String DEACTIVATE_DRIVER = "Deactivating a driver with id '%d'";
    public final String DRIVER_WAS_DEACTIVATED = "The driver with id '%d' was deactivated.";
    public final String FIND_DRIVER_PROFILE = "Finding a profile of the driver with id '%d'";
    public final String CREATING_DRIVER_ALREADY_EXISTS = "Creating driver already exists (existing parameter is '%s')";
}
