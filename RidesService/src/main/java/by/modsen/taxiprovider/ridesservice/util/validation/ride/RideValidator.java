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

        String status = ride.getStatus();
        if ((!status.equals("In process")) && (!status.equals("Completed")) && (!status.equals("Deleted"))) {
            errors.rejectValue("status", "", "Wrong ride's status");
        }
    }
}
