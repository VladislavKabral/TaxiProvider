package by.modsen.taxiprovider.passengerservice.dto.passenger;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

    @Size(min = 2, max = 50, message = "Passenger's lastname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Passenger's lastname must contain only letters")
    private String lastname;

    @Size(min = 2, max = 50, message = "Passenger's firstname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Passenger's firstname must contain only letters")
    private String firstname;

    @Email(message = "Wrong email format")
    private String email;

    private String role;

    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", message = "Wrong phone number format")
    private String phoneNumber;
}
