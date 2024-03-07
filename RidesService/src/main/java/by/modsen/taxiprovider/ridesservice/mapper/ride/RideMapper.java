package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RideMapper {

    private final ModelMapper modelMapper;

    public Ride toEntity(RideDTO rideDTO) {
        Ride ride = modelMapper.map(rideDTO, Ride.class);

        ride.setStartedAt(rideDTO.getStartedAt()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime());
        ride.setEndedAt(rideDTO.getEndedAt()
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime());

        return ride;
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
