package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
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
        RideDTO ride = (RideDTO) target;

        String status = ride.getStatus();
        if ((!status.equals("IN_PROGRESS")) && (!status.equals("COMPLETED")) && (!status.equals("CANCELLED"))) {
            errors.rejectValue("status", "", "Wrong ride's status");
        }
    }
}
