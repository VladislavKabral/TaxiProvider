package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class RideValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Ride.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Ride ride = (Ride) target;

        if (ride.getStartedAt().isAfter(ride.getEndedAt())) {
            errors.rejectValue("startedAt", "", "Ride can't start after ending." +
                    " Check startedAt field");
        }
    }
}
