package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerChargeRequestDto {

    private static final String AMOUNT_MINIMAL_VALUE = "0.01";

    @NotNull(message = TAXI_USER_ID_IS_NULL)
    @Min(value = 1, message = TAXI_USER_ID_IS_INVALID)
    private long taxiUserId;

    @NotNull(message = AMOUNT_IS_NULL)
    @DecimalMin(value = AMOUNT_MINIMAL_VALUE, message = AMOUNT_MINIMAL_VALUE_IS_INVALID)
    private BigDecimal amount;

    @NotBlank(message = CURRENCY_IS_EMPTY)
    private String currency;

    @NotBlank(message = TAXI_USER_ROLE_IS_EMPTY)
    private String role;
}
