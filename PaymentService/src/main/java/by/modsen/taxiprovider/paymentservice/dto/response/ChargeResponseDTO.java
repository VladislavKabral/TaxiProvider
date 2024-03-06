package by.modsen.taxiprovider.paymentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeResponseDTO {

    private String currency;

    private BigDecimal amount;

    private String status;
}
