package by.modsen.taxiprovider.passengerservice.dto.passenger;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.util.Regex.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

    private long id;

    @Size(min = 2, max = 50, message = PASSENGER_LASTNAME_SIZE_IS_INVALID)
    @Pattern(regexp = PASSENGER_LASTNAME_FIRSTNAME_REGEXP, message = PASSENGER_LASTNAME_BODY_IS_INVALID)
    private String lastname;

    @Size(min = 2, max = 50, message = PASSENGER_FIRSTNAME_SIZE_IS_INVALID)
    @Pattern(regexp = PASSENGER_LASTNAME_FIRSTNAME_REGEXP, message = PASSENGER_FIRSTNAME_BODY_IS_INVALID)
    private String firstname;

    @Email(message = PASSENGER_EMAIL_WRONG_FORMAT)
    private String email;

    @Pattern(regexp = PASSENGER_PHONE_NUMBER_REGEXP, message = PASSENGER_PHONE_NUMBER_FORMAT_IS_WRONG)
    private String phoneNumber;
}
