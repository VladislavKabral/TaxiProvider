package by.modsen.taxiprovider.ridesservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {
    public final String DRIVER_LASTNAME_SIZE_IS_INVALID = "Driver's lastname must be between 2 and 50 symbols";
    public final String DRIVER_LASTNAME_BODY_IS_INVALID = "Driver's lastname must contain only letters";
    public final String DRIVER_LASTNAME_IS_EMPTY = "Driver's lastname must be not empty";
    public final String DRIVER_FIRSTNAME_SIZE_IS_INVALID = "Driver's firstname must be between 2 and 50 symbols";
    public final String PROMO_CODE_SIZE_IS_INVALID = "Promo code must be between 2 and 50 symbols";
    public final String DRIVER_FIRSTNAME_BODY_IS_INVALID = "Driver's firstname must contain only letters";
    public final String PROMO_CODE_BODY_IS_INVALID = "Promo code must contains only letters and numbers";
    public final String DRIVER_FIRSTNAME_IS_EMPTY = "Driver's firstname must be not empty";
    public final String DRIVER_EMAIL_WRONG_FORMAT = "Wrong email format";
    public final String DRIVER_EMAIL_IS_EMPTY = "Driver's email must be not empty";
    public final String DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG = "Wrong phone number format";
    public final String DRIVER_PHONE_NUMBER_IS_EMPTY = "Driver's phone number must be not empty";
    public final String ADDRESS_LATITUDE_IS_NULL = "Latitude must be not empty";
    public final String ADDRESS_LONGITUDE_IS_NULL = "Longitude must be not empty";
    public final String LATITUDE_MINIMAL_VALUE_IS_INVALID = "Minimal value for latitude is '-90'";
    public final String LATITUDE_MAXIMUM_VALUE_IS_INVALID = "Maximum value for latitude is '90'";
    public final String LONGITUDE_MINIMAL_VALUE_IS_INVALID = "Minimal value for longitude is '-180'";
    public final String LONGITUDE_MAXIMUM_VALUE_IS_INVALID = "Maximum value for longitude is '180'";
    public final String PASSENGER_ID_MINIMAL_VALUE_IS_INVALID = "Passenger's id must be a number and can't be less than one";
    public final String SOURCE_ADDRESS_IS_EMPTY = "Source address must be not empty";
    public final String DESTINATION_ADDRESS_IS_EMPTY = "Destination address-(es) must be not empty";
    public final String DESTINATION_ADDRESSES_COUNT_IS_INVALID = "Must be at least one destination address";
    public final String PASSENGER_ID_IS_INVALID = "Passenger's id must be a number and can't be less than one";
    public final String DRIVER_ID_IS_INVALID = "Driver's id must be a number and can't be less than one";
    public final String PROMO_CODES_NOT_FOUND = "There aren't any promo codes";
    public final String PROMO_CODE_WITH_VALUE_NOT_FOUND = "Promo code '%s' wasn't found";
    public final String PROMO_CODE_WITH_ID_NOT_FOUND = "Promo code with id '%d' wasn't found";
    public final String PROMO_CODE_NOT_CREATED = "Promo code with id '%s' wasn't created";
    public final String DISTANCE_CALCULATION_IS_FAILED = "Impossible to calculate the distance of the ride";
    public final String RIDES_NOT_FOUND = "There aren't any rides";
    public final String RIDE_NOT_FOUND = "Ride with id '%d' wasn't found";
    public final String RIDE_NOT_CREATED = "Ride with driver's id '%d' wasn't created";
    public final String PASSENGER_RIDES_NOT_FOUND = "Passenger with id '%d' doesn't have any rides";
    public final String DRIVER_RIDES_NOT_FOUND = "Driver with id '%d' doesn't have any rides";
    public final String DRIVER_CURRENT_RIDES_NOT_FOUND = "Current driver doesn't have active rides";
    public final String WAITING_RIDES_NOT_FOUND = "Cannot find rides with status 'WAITING'";
    public final String DRIVER_NOT_FOUND = "Cannot find driver with id '%d'";
}
