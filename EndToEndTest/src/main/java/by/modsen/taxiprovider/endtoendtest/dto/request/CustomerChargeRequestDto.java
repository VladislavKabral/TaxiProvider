package by.modsen.taxiprovider.endtoendtest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerChargeRequestDto {

    private static final String AMOUNT_MINIMAL_VALUE = "0.01";

    private long taxiUserId;

    private BigDecimal amount;

    private String currency;

    private String role;
}
