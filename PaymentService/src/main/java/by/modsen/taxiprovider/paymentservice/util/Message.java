package by.modsen.taxiprovider.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Message {
    public final String MONTH_MINIMAL_VALUE_IS_INVALID = "Minimal value of month's number is '1'";
    public final String MONTH_MAXIMUM_VALUE_IS_INVALID = "Maximum value of month's number is '12'";
    public final String CVC_MINIMAL_VALUE_IS_INVALID = "Minimal value of month's number is '1'";
    public final String CVC_MAXIMUM_VALUE_IS_INVALID = "Minimal value of month's number is '1'";
    public final String CARD_NUMBER_IS_EMPTY = "Card's number must be not empty";
    public final String EXPIRATION_MONTH_IS_NULL = "Expiration month can't be null";
    public final String EXPIRATION_YEAR_IS_NULL = "Expiration year can't be null";
    public final String CVC_IS_NULL = "CVC code can't be null";
    public final String AMOUNT_IS_NULL = "Amount must be not null";
    public final String AMOUNT_MINIMAL_VALUE_IS_INVALID = "Minimal value of amount is '0.01'";
    public final String CARD_NUMBER_BODY_IS_INVALID = "Wrong format of card's number";
    public final String CURRENCY_IS_EMPTY = "Currency must be not empty";
    public final String CARD_TOKEN_IS_EMPTY = "Card's token must be not empty";
    public final String TAXI_USER_ROLE_IS_EMPTY = "Taxi user's role must be not empty";
    public final String TAXI_USER_ID_IS_NULL = "Taxi user's id must be not null";
    public final String TAXI_USER_ID_IS_INVALID = "Minimal value of taxi user's id is '1'";
    public final String CUSTOMER_NAME_IS_EMPTY = "Customer's name must be not empty";
    public final String CUSTOMER_NAME_IS_INVALID = "Customer's name can contain only letters and spaces";
    public final String CUSTOMER_EMAIL_IS_EMPTY = "Customer's email must be not empty";
    public final String CUSTOMER_EMAIL_IS_INVALID = "Wrong email format";
    public final String CUSTOMER_PHONE_NUMBER_IS_EMPTY = "Customer's phone number must be not empty";
    public final String CUSTOMER_PHONE_NUMBER_IS_INVALID = "Wrong format of phone number";
    public final String BALANCE_IS_NULL = "Balance must be not null";
    public final String BALANCE_VALUE_IS_INVALID = "Minimal value of balance is '0.01'";
    public final String USER_NOT_FOUND = "User with id '%d' and role '%s' wasn't found";
    public final String CUSTOMER_NOT_FOUND = "Customer with email '%s' wasn't created";
    public final String NOT_ENOUGH_MONEY_ON_BALANCE = "Not enough money on the balance";
    public final String EXTERNAL_SERVICE_IS_UNAVAILABLE = "Cannot connect to rating's service";
    public final String EXTERNAL_SERVICE_ERROR = "Cannot get response from the driver's service";
    public final String PASSENGER_ID_IS_INVALID = "Passenger's id must be a number and can't be less than one";
    public final String DRIVER_ID_IS_INVALID = "Driver's id must be a number and can't be less than one";
    public final String CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE = "External service '%s' failed to process after max retries";
    public final String BANK_CARD_EXPIRATION_IS_INVALID = "Bank card's expiration is invalid";
}
