package by.modsen.taxiprovider.paymentservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerChargeRequestDTO {

    private long taxiUserId;

    private BigDecimal amount;

    private String currency;
}
