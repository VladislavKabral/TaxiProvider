package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.paymentservice.util.Regex.*;
import static by.modsen.taxiprovider.paymentservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequestDto {

    private static final int CVC_MINIMAL_VALUE = 0;

    private static final int CVC_MAXIMUM_VALUE = 999;

    private static final int MONTH_MINIMUM_VALUE = 1;

    private static final int MONTH_MAXIMUM_VALUE = 12;

    @NotBlank(message = CARD_NUMBER_IS_EMPTY)
    @Pattern(regexp = PAYMENT_CARD_REGEXP, message = CARD_NUMBER_BODY_IS_INVALID)
    private String number;

    @Min(value = MONTH_MINIMUM_VALUE, message = MONTH_MINIMAL_VALUE_IS_INVALID)
    @Max(value = MONTH_MAXIMUM_VALUE, message = MONTH_MAXIMUM_VALUE_IS_INVALID)
    @NotNull(message = EXPIRATION_MONTH_IS_NULL)
    private int month;

    @NotNull(message = EXPIRATION_YEAR_IS_NULL)
    private int year;

    @NotNull(message = CVC_IS_NULL)
    @Min(value = CVC_MINIMAL_VALUE, message = CVC_MINIMAL_VALUE_IS_INVALID)
    @Max(value = CVC_MAXIMUM_VALUE, message = CVC_MAXIMUM_VALUE_IS_INVALID)
    private int cvc;
}
