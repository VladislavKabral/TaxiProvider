package by.modsen.taxiprovider.passengerservice.mapper.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.model.passenger.PassengerProfile;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassengerProfileMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PassengerProfileMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PassengerProfileDTO toDTO(PassengerProfile passengerProfile) {
        return modelMapper.map(passengerProfile, PassengerProfileDTO.class);
    }
}
