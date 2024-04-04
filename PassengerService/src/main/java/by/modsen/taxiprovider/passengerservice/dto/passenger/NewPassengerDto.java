package by.modsen.taxiprovider.passengerservice.dto.passenger;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.util.Regex.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPassengerDto {

    @NotBlank(message = PASSENGER_LASTNAME_IS_EMPTY)
    @Size(min = 2, max = 50, message = PASSENGER_LASTNAME_SIZE_IS_INVALID)
    @Pattern(regexp = PASSENGER_LASTNAME_FIRSTNAME_REGEXP, message = PASSENGER_LASTNAME_BODY_IS_INVALID)
    private String lastname;

    @NotBlank(message = PASSENGER_FIRSTNAME_IS_EMPTY)
    @Size(min = 2, max = 50, message = PASSENGER_FIRSTNAME_SIZE_IS_INVALID)
    @Pattern(regexp = PASSENGER_LASTNAME_FIRSTNAME_REGEXP, message = PASSENGER_FIRSTNAME_BODY_IS_INVALID)
    private String firstname;

    @NotBlank(message = PASSENGER_EMAIL_IS_EMPTY)
    @Email(message = PASSENGER_EMAIL_WRONG_FORMAT)
    private String email;

    @NotBlank(message = PASSENGER_PASSWORD_IS_EMPTY)
    private String password;

    @NotBlank(message = PASSENGER_PHONE_NUMBER_IS_EMPTY)
    @Pattern(regexp = PASSENGER_PHONE_NUMBER_REGEXP, message = PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG)
    private String phoneNumber;
}
