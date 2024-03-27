package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverBalanceRequestDTO {

    private static final String AMOUNT_MINIMAL_VALUE = "0.01";

    @NotNull(message = AMOUNT_IS_NULL)
    @DecimalMin(value = AMOUNT_MINIMAL_VALUE, message = AMOUNT_MINIMAL_VALUE_IS_INVALID)
    private BigDecimal amount;
}
