package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeRequestDTO {

    @NotNull(message = "Amount must be not null")
    @DecimalMin(value = "0.01", message = "Minimal value of amount is '0.01'")
    private BigDecimal amount;

    @NotBlank(message = "Currency must be not empty")
    private String currency;

    @NotBlank(message = "Card's token must be not empty")
    private String cardToken;
}