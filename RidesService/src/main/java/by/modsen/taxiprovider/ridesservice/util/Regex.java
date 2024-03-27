package by.modsen.taxiprovider.ridesservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Regex {

    public final String DRIVER_LASTNAME_FIRSTNAME_REGEXP = "^[a-zA-Z ]+$";

    public final String DRIVER_PHONE_NUMBER_REGEXP = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$";

    public final String PROMO_CODE_VALUE_REGEXP = "^[a-zA-Z0-9]+$";
}
