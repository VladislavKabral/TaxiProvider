package by.modsen.taxiprovider.passengerservice.model.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerProfile {

    private Passenger passenger;

    private Double rating;
}
