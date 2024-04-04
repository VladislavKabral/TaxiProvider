package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RideMapper {

    private final ModelMapper modelMapper;

    public Ride toEntity(NewRideDto rideDTO) {
        return modelMapper.map(rideDTO, Ride.class);
    }

    public RideDto toDTO(Ride ride) {
        return modelMapper.map(ride, RideDto.class);
    }

    public List<RideDto> toListDTO(List<Ride> rides) {
        return rides.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
