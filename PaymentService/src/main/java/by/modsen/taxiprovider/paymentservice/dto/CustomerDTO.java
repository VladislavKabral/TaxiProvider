package by.modsen.taxiprovider.paymentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    @NotBlank(message = "Customer's name must be not empty")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Customer's name can contain only letters and spaces")
    private String name;

    @NotBlank(message = "Customer's email must be not empty")
    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "Customer's phone number must be not empty")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", message = "Wrong format of phone number")
    private String phone;

    @NotNull(message = "Taxi user's id must be not null")
    @Min(value = 1, message = "Minimal value of taxi user's id is '1'")
    private long taxiUserId;

    @NotNull(message = "Balance must be not null")
    @DecimalMin(value = "0.01", message = "Minimal value of balance is '0.01'")
    private BigDecimal balance;
}
