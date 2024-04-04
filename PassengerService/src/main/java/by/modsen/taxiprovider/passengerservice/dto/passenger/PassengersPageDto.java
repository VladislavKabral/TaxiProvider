package by.modsen.taxiprovider.passengerservice.dto.passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengersPageDto {

    private List<PassengerDTO> content;

    private int page;

    private int size;
}
