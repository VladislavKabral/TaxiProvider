package by.modsen.taxiprovider.passengerservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {

    public final String PASSENGERS_NOT_FOUND = "There aren't any passengers";
    public final String INVALID_PAGE_REQUEST = "Number of page and count of elements can't be less than zero";
    public final String PASSENGERS_ON_PAGE_NOT_FOUND = "There aren't any passengers on this page";
    public final String PASSENGER_NOT_FOUND = "Passenger with id '%d' wasn't found";
    public final String PASSENGER_NOT_CREATED = "Passenger with email '%s' wasn't created";
    public final String PASSENGER_ID_IS_NULL = "Passenger's id must be not null";
    public final String PASSENGER_ID_MINIMAL_VALUE = "Minimal value of passenger's id is '1'";
    public final String PASSENGER_ROLE_IS_EMPTY = "Passenger's role must be not empty";
    public final String PASSENGER_LASTNAME_SIZE_IS_INVALID = "Passenger's lastname must be between 2 and 50 symbols";
    public final String PASSENGER_LASTNAME_BODY_IS_INVALID = "Passenger's lastname must contain only letters";
    public final String PASSENGER_LASTNAME_IS_EMPTY = "Passenger's lastname must be not empty";
    public final String PASSENGER_FIRSTNAME_SIZE_IS_INVALID = "Passenger's firstname must be between 2 and 50 symbols";
    public final String PASSENGER_FIRSTNAME_BODY_IS_INVALID = "Passenger's firstname must contain only letters";
    public final String PASSENGER_FIRSTNAME_IS_EMPTY = "Passenger's firstname must be not empty";
    public final String PASSENGER_EMAIL_WRONG_FORMAT = "Wrong email format";
    public final String PASSENGER_EMAIL_IS_EMPTY = "Passenger's email must be not empty";
    public final String PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG = "Wrong phone number format";
    public final String PASSENGER_PHONE_NUMBER_IS_EMPTY = "Passenger's phone number must be not empty";
    public final String PASSENGER_PASSWORD_IS_EMPTY = "Passenger's password must be not empty";
}
