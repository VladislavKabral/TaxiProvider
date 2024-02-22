package by.modsen.taxiprovider.passengerservice.mapper.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PassengerMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PassengerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Passenger toEntity(PassengerDTO passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }

    public Passenger toEntity(NewPassengerDTO passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }

    public PassengerDTO toDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    public List<PassengerDTO> toListDTO(List<Passenger> passengers) {
        return passengers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
