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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequestDTO {

    @NotBlank(message = "Card's number must be not empty")
    @Pattern(regexp = "(\\d{4}(-|)\\d{4}(-|)\\d{4}(-|)\\d{4})", message = "Wrong format of card's number")
    private String number;

    @Min(value = 1, message = "Minimal value of month's number is '1'")
    @Max(value = 12, message = "Maximum value of month's number is '12'")
    @NotNull(message = "Expiration month can't be null")
    private int month;

    @NotNull(message = "Expiration year can't be null")
    private int year;

    @NotNull(message = "CVC code can't be null")
    @Min(value = 0, message = "Minimal value of CVC code is '000'")
    @Max(value = 999, message = "Maximum value of CVC code is '999'")
    private int cvc;
}
