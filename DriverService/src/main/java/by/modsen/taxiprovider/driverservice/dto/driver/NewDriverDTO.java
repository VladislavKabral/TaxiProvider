package by.modsen.taxiprovider.driverservice.dto.driver;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewDriverDTO {

    @Size(min = 2, max = 50, message = "Driver's lastname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Driver's lastname must contain only letters")
    @NotEmpty(message = "Driver's lastname must be not empty")
    private String lastname;

    @Size(min = 2, max = 50, message = "Driver's firstname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Driver's firstname must contain only letters")
    @NotEmpty(message = "Driver's firstname must be not empty")
    private String firstname;

    @Email(message = "Wrong email format")
    @NotEmpty(message = "Driver's email must be not empty")
    private String email;

    @NotEmpty(message = "Passenger's password must be not empty")
    private String password;

    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", message = "Wrong phone number format")
    @NotEmpty(message = "Driver's phone number must be not empty")
    private String phoneNumber;
}
