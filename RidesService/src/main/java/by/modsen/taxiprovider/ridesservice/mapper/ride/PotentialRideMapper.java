package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDTO;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PotentialRideMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PotentialRideMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PotentialRide toEntity(PotentialRideDTO potentialRideDTO) {
        return modelMapper.map(potentialRideDTO, PotentialRide.class);
    }
}
