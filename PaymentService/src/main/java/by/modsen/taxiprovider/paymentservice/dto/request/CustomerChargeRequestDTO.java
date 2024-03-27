package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerChargeRequestDTO {

    @NotNull(message = "Taxi user's id must be not null")
    @Min(value = 1, message = "Minimal value of taxi user's id is '1'")
    private long taxiUserId;

    @NotNull(message = "Amount must be not null")
    @DecimalMin(value = "0.01", message = "Minimal value of amount is '0.01'")
    private BigDecimal amount;

    @NotBlank(message = "Currency must be not empty")
    private String currency;

    @NotBlank(message = "Taxi user's role must be not empty")
    private String role;
}
