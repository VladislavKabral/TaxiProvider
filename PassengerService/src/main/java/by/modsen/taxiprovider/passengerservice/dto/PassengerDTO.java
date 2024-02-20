package by.modsen.taxiprovider.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

    private String lastname;

    private String firstname;

    private String email;

    private String role;

    private String phoneNumber;
}
