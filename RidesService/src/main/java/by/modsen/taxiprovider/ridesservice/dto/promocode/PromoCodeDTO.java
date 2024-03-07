package by.modsen.taxiprovider.ridesservice.dto.promocode;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO {

    @Size(min = 2, max = 50, message = "Promo code must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Promo code must contains only letters and numbers")
    private String value;

    private double discount;
}
