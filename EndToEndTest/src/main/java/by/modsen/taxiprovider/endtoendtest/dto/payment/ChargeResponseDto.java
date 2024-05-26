package by.modsen.taxiprovider.endtoendtest.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeResponseDto {

    private String currency;

    private BigDecimal amount;

    private String status;
}
