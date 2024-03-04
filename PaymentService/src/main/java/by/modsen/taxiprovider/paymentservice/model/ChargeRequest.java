package by.modsen.taxiprovider.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChargeRequest {

    private String description;

    private int amount;

    private String currency;

    private String stripeEmail;

    private String stripeToken;
}
