package by.modsen.taxiprovider.passengerservice.dto.passenger;

import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerProfileDTO {

    private PassengerDTO passenger;

    private Double rating;
}
