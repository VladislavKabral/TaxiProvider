package by.modsen.taxiprovider.driverservice.util.validation;

import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.driverservice.util.Message.*;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DriversValidator {

    private final DriversRepository driversRepository;

    public void validate(Driver driver) throws EntityValidateException {
        Optional<Driver> existingDriver = driversRepository.findByEmail(driver.getEmail());
        if ((existingDriver.isPresent()) && (existingDriver.get().getId() != driver.getId())) {
            throw new EntityValidateException(String.format(DRIVER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, driver.getEmail()));
        }

        existingDriver = driversRepository.findByPhoneNumber(driver.getPhoneNumber());
        if ((existingDriver.isPresent()) && (existingDriver.get().getId() != driver.getId())) {
            throw new EntityValidateException(String.format(DRIVER_WITH_GIVEN_PHONE_NUMBER_ALREADY_EXISTS,
                    driver.getPhoneNumber()));
        }

    }
}
