package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.dto.driver.DriverRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DriverRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return DriverRequestDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DriverRequestDTO driverRequestDTO = (DriverRequestDTO) target;

        if ((!driverRequestDTO.getRideStatus().equals("Start")) && (!driverRequestDTO.getRideStatus().equals("End"))) {
            errors.rejectValue("rideStatus", "", "Wrong ride status from driver");
        }
    }
}
