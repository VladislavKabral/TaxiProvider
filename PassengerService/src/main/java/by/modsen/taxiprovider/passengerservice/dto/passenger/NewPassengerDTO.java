package by.modsen.taxiprovider.passengerservice.dto.passenger;

import by.modsen.taxiprovider.passengerservice.dto.card.CreditCardDTO;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPassengerDTO {

    @NotEmpty(message = "Passenger's lastname must be not empty")
    @Size(min = 2, max = 50, message = "Passenger's lastname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Passenger's lastname must contain only letters")
    private String lastname;

    @NotEmpty(message = "Passenger's firstname must be not empty")
    @Size(min = 2, max = 50, message = "Passenger's firstname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Passenger's firstname must contain only letters")
    private String firstname;

    @NotEmpty(message = "Passenger's email must be not empty")
    @Email(message = "Wrong email format")
    private String email;

    @NotEmpty(message = "Passenger's password must be not empty")
    private String password;

    @NotEmpty(message = "Phone number must be not empty")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", message = "Wrong phone number format")
    private String phoneNumber;

    @NotNull
    private CreditCardDTO creditCard;
}
