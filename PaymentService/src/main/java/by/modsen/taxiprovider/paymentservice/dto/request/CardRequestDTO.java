package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.*;
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

    @Pattern(regexp = "^[0-9]{3}$", message = "Wrong CVC code format")
    @NotNull(message = "CVC code can't be null")
    private int cvc;
}
