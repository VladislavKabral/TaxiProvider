package by.modsen.taxiprovider.passengerservice.dto.card;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO {

    @Pattern(regexp = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", message = "Wrong account number format")
    @NotEmpty
    private String number;
}
