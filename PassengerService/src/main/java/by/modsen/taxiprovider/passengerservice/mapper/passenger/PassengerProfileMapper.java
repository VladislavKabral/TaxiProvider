package by.modsen.taxiprovider.passengerservice.mapper.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.model.passenger.PassengerProfile;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PassengerProfileMapper {

    private final ModelMapper modelMapper;

    public PassengerProfileDTO toDTO(PassengerProfile passengerProfile) {
        return modelMapper.map(passengerProfile, PassengerProfileDTO.class);
    }
}
