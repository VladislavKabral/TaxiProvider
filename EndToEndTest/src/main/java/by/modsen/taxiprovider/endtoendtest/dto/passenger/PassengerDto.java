package by.modsen.taxiprovider.endtoendtest.dto.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerDto {

    private long id;

    private String lastname;

    private String firstname;

    private String email;

    private String phoneNumber;
}
