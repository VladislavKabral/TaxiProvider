package by.modsen.taxiprovider.passengerservice.util.validation;

import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PassengersValidator implements Validator {

    private final PassengersRepository passengersRepository;

    private static final String EMAIL_FIELD_NAME = "email";

    private static final String PHONE_NUMBER_FIELD_NAME = "phoneNumber";

    @Override
    public boolean supports(Class<?> clazz) {
        return Passenger.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Passenger passenger = (Passenger) target;

        Optional<Passenger> existingPassenger = passengersRepository.findByEmail(passenger.getEmail());
        if ((existingPassenger.isPresent()) && (existingPassenger.get().getId() != passenger.getId())) {
            errors.rejectValue(EMAIL_FIELD_NAME, "",
                    String.format(PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS, passenger.getEmail()));
        }

        existingPassenger = passengersRepository.findByPhoneNumber(passenger.getPhoneNumber());
        if ((existingPassenger.isPresent()) && (existingPassenger.get().getId() != passenger.getId())) {
            errors.rejectValue(PHONE_NUMBER_FIELD_NAME, "",
                    String.format(PASSENGER_WITH_GIVEN_PHONE_NUMBER_ALREADY_EXISTS, passenger.getPhoneNumber()));
        }
    }
}
