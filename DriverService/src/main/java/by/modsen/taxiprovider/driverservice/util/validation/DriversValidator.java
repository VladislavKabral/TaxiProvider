package by.modsen.taxiprovider.driverservice.util.validation;

import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static by.modsen.taxiprovider.driverservice.util.Message.*;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriversValidator implements Validator {

    private final DriversRepository driversRepository;

    private static final String EMAIL_FIELD_NAME = "email";

    private static final String PHONE_NUMBER_FIELD_NAME = "phoneNumber";

    @Override
    public boolean supports(Class<?> clazz) {
        return Driver.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Driver driver = (Driver) target;

        Optional<Driver> existingDriver = driversRepository.findByEmail(driver.getEmail());
        if ((existingDriver.isPresent()) && (existingDriver.get().getId() != driver.getId())) {
            errors.rejectValue(EMAIL_FIELD_NAME, "",
                    String.format(DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, driver.getEmail()));
        }

        existingDriver = driversRepository.findByPhoneNumber(driver.getPhoneNumber());
        if ((existingDriver.isPresent()) && (existingDriver.get().getId() != driver.getId())) {
            errors.rejectValue(PHONE_NUMBER_FIELD_NAME, "",
                    String.format(DRIVER_WITH_GIVEN_PHONE_NUMBER_ALREADY_EXISTS, driver.getPhoneNumber()));
        }
    }
}
