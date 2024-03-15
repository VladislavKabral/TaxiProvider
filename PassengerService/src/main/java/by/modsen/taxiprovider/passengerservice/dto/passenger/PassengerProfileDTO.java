package by.modsen.taxiprovider.passengerservice.dto.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerProfileDTO {

    private PassengerDTO passenger;

    private Double rating;
}
