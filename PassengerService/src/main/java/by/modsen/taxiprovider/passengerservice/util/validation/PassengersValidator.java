package by.modsen.taxiprovider.passengerservice.util.validation;

import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PassengersValidator implements Validator {

    private final PassengersRepository passengersRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Passenger.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Passenger passenger = (Passenger) target;

        Optional<Passenger> existingPassenger = passengersRepository.findByEmail(passenger.getEmail());
        if ((existingPassenger.isPresent()) && (existingPassenger.get().getId() != passenger.getId())) {
            errors.rejectValue("email", "", "A passenger with email '" +
                    passenger.getEmail() + "' already exists");
        }

        existingPassenger = passengersRepository.findByPhoneNumber(passenger.getPhoneNumber());
        if ((existingPassenger.isPresent()) && (existingPassenger.get().getId() != passenger.getId())) {
            errors.rejectValue("phoneNumber", "", "A passenger with phone number '" +
                    passenger.getPhoneNumber() + "' already exists");
        }
    }
}
