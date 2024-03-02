package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import by.modsen.taxiprovider.ridesservice.mapper.ride.RideMapper;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class RideValidator implements Validator {

    private final RideMapper rideMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return RideDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ride ride = rideMapper.toEntity((RideDTO) target);

        if (ride.getStartedAt().isAfter(ride.getEndedAt())) {
            errors.rejectValue("startedAt", "", "Ride can't start after ending." +
                    " Check startedAt field");
        }
    }
}
