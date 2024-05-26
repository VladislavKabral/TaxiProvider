package by.modsen.taxiprovider.paymentservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Regex {

    public final String PAYMENT_CARD_REGEXP = "(\\d{4}(-|)\\d{4}(-|)\\d{4}(-|)\\d{4})";
    public final String CUSTOMER_NAME_REGEXP = "^[a-zA-Z ]+$";
    public final String CUSTOMER_PHONE_NUMBER_REGEXP = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$";
}
