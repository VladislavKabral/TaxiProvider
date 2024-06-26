package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDto;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PotentialRideMapper {

    private final ModelMapper modelMapper;

    public PotentialRide toEntity(PotentialRideDto potentialRideDto) {
        return modelMapper.map(potentialRideDto, PotentialRide.class);
    }
}
