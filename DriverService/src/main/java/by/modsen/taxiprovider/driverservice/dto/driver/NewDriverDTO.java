package by.modsen.taxiprovider.driverservice.dto.driver;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static by.modsen.taxiprovider.driverservice.util.Regex.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewDriverDTO {

    @Size(min = 2, max = 50, message = DRIVER_LASTNAME_SIZE_IS_INVALID)
    @Pattern(regexp = DRIVER_LASTNAME_FIRSTNAME_REGEXP, message = DRIVER_LASTNAME_BODY_IS_INVALID)
    @NotEmpty(message = DRIVER_LASTNAME_IS_EMPTY)
    private String lastname;

    @Size(min = 2, max = 50, message = DRIVER_FIRSTNAME_SIZE_IS_INVALID)
    @Pattern(regexp = DRIVER_LASTNAME_FIRSTNAME_REGEXP, message = DRIVER_FIRSTNAME_BODY_IS_INVALID)
    @NotEmpty(message = DRIVER_FIRSTNAME_IS_EMPTY)
    private String firstname;

    @Email(message = DRIVER_EMAIL_WRONG_FORMAT)
    @NotEmpty(message = DRIVER_EMAIL_IS_EMPTY)
    private String email;

    @NotEmpty(message = DRIVER_PASSWORD_IS_EMPTY)
    private String password;

    @Pattern(regexp = DRIVER_PHONE_NUMBER_REGEXP, message = DRIVER_PHONE_NUMBER_FORMAT_IS_WRONG)
    @NotEmpty(message = DRIVER_PHONE_NUMBER_IS_EMPTY)
    private String phoneNumber;
}
