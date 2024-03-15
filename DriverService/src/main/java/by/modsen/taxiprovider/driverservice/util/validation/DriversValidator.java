package by.modsen.taxiprovider.driverservice.util.validation;

import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriversValidator implements Validator {

    private final DriversRepository driversRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Driver.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Driver driver = (Driver) target;

        Optional<Driver> existingDriver = driversRepository.findByEmail(driver.getEmail());
        if ((existingDriver.isPresent()) && (existingDriver.get().getId() != driver.getId())) {
            errors.rejectValue("email", "", "Driver with email '" + driver.getEmail() +
                    "' already exists");
        }

        existingDriver = driversRepository.findByPhoneNumber(driver.getPhoneNumber());
        if ((existingDriver.isPresent()) && (existingDriver.get().getId() != driver.getId())) {
            errors.rejectValue("phoneNumber", "", "Driver with phone number '" +
                    driver.getPhoneNumber() + "' already exists");
        }
    }
}
