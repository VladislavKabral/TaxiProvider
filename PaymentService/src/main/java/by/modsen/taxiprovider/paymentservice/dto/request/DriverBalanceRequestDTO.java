package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverBalanceRequestDTO {

    @NotNull(message = "Amount must be not null")
    @DecimalMin(value = "0.01", message = "Minimal value of amount is '0.01'")
    private BigDecimal amount;
}
