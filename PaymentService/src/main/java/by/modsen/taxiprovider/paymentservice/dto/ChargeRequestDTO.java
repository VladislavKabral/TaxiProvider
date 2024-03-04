package by.modsen.taxiprovider.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChargeRequestDTO {

    private String description;

    private int amount;

    private String currency;

    private String stripeEmail;

    private String stripeToken;
}
