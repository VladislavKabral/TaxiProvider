package by.modsen.taxiprovider.driverservice.util.validation;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class DriversValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return DriverDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

    }
}
