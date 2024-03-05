package by.modsen.taxiprovider.paymentservice.dto;

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

    private BigDecimal amount;

    private String currency;

    private String cardToken;
}
