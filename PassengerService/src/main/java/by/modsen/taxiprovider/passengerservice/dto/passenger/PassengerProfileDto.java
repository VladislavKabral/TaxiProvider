package by.modsen.taxiprovider.passengerservice.dto.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerProfileDto {

    private PassengerDto passenger;

    private Double rating;
}
