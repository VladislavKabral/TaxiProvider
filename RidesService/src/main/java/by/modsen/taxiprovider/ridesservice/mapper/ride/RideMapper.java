package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
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

    public Ride toEntity(NewRideDTO rideDTO) {
        return modelMapper.map(rideDTO, Ride.class);
    }

    public RideDTO toDTO(Ride ride) {
        return modelMapper.map(ride, RideDTO.class);
    }

    public List<RideDTO> toListDTO(List<Ride> rides) {
        return rides.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
