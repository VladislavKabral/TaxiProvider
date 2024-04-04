package by.modsen.taxiprovider.passengerservice.mapper;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PassengerMapper {

    private final ModelMapper modelMapper;

    public Passenger toEntity(PassengerDto passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }

    public Passenger toEntity(NewPassengerDto passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }

    public PassengerDto toDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDto.class);
    }

    public List<PassengerDto> toListDTO(List<Passenger> passengers) {
        return passengers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
