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

    public Passenger toEntity(PassengerDto passengerDto) {
        return modelMapper.map(passengerDto, Passenger.class);
    }

    public Passenger toEntity(NewPassengerDto passengerDto) {
        return modelMapper.map(passengerDto, Passenger.class);
    }

    public PassengerDto toDto(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDto.class);
    }

    public List<PassengerDto> toListDto(List<Passenger> passengers) {
        return passengers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
