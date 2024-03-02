package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDTO;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PotentialRideMapper {

    private final ModelMapper modelMapper;

    public PotentialRide toEntity(PotentialRideDTO potentialRideDTO) {
        return modelMapper.map(potentialRideDTO, PotentialRide.class);
    }

    public PotentialRideDTO toDTO(PotentialRide potentialRide) {
        return modelMapper.map(potentialRide, PotentialRideDTO.class);
    }
}
