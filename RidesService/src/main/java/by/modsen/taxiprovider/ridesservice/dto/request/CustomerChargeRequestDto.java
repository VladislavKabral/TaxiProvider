package by.modsen.taxiprovider.ridesservice.dto.request;

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

    private long taxiUserId;

    private BigDecimal amount;

    private String currency;

    private String role;
}
