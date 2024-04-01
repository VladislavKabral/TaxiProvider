package by.modsen.taxiprovider.ridesservice.dto.promocode;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.util.Regex.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDto {

    private long id;

    @Size(min = 2, max = 50, message = PROMO_CODE_SIZE_IS_INVALID)
    @Pattern(regexp = PROMO_CODE_VALUE_REGEXP, message = PROMO_CODE_BODY_IS_INVALID)
    private String value;

    private double discount;
}
