package by.modsen.taxiprovider.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;
import static by.modsen.taxiprovider.paymentservice.util.Regex.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private static final String BALANCE_MINIMAL_VALUE = "0.00";

    @NotBlank(message = CUSTOMER_NAME_IS_EMPTY)
    @Pattern(regexp = CUSTOMER_NAME_REGEXP, message = CUSTOMER_NAME_IS_INVALID)
    private String name;

    @NotBlank(message = CUSTOMER_EMAIL_IS_EMPTY)
    @Email(message = CUSTOMER_EMAIL_IS_INVALID)
    private String email;

    @NotBlank(message = CUSTOMER_PHONE_NUMBER_IS_EMPTY)
    @Pattern(regexp = CUSTOMER_PHONE_NUMBER_REGEXP, message = CUSTOMER_PHONE_NUMBER_IS_INVALID)
    private String phone;

    @NotNull(message = TAXI_USER_ID_IS_NULL)
    @Min(value = 1, message = TAXI_USER_ID_IS_INVALID)
    private long taxiUserId;

    @NotNull(message = BALANCE_IS_NULL)
    @DecimalMin(value = BALANCE_MINIMAL_VALUE, message = BALANCE_VALUE_IS_INVALID)
    private BigDecimal balance;

    @NotBlank(message = TAXI_USER_ROLE_IS_EMPTY)
    private String role;
}
