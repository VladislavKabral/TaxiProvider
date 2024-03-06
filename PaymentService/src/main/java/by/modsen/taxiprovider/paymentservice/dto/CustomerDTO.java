package by.modsen.taxiprovider.paymentservice.dto;

import jakarta.validation.constraints.*;
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
    private String phone;

    @NotNull(message = "Taxi user's id must be not null")
    @Min(value = 1, message = "Minimal value of taxi user's id is '1'")
    private long taxiUserId;

    @NotNull(message = "Balance must be not null")
    @DecimalMin(value = "0.01", message = "Minimal value of balance is '0.01'")
    private BigDecimal balance;
}
