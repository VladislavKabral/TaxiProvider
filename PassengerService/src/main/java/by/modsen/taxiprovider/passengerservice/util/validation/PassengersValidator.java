package by.modsen.taxiprovider.passengerservice.util.validation;

import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PassengersValidator {

    private final PassengersRepository passengersRepository;

    public void validate(Passenger passenger) throws EntityValidateException {
        Optional<Passenger> existingPassenger = passengersRepository.findByEmail(passenger.getEmail());
        if ((existingPassenger.isPresent()) && (existingPassenger.get().getId() != passenger.getId())) {
            throw new EntityValidateException(String.format(PASSENGER_WITH_GIVEN_EMAIL_ALREADY_EXISTS,
                    passenger.getEmail()));
        }

        existingPassenger = passengersRepository.findByPhoneNumber(passenger.getPhoneNumber());
        if ((existingPassenger.isPresent()) && (existingPassenger.get().getId() != passenger.getId())) {
            throw new EntityValidateException(String.format(PASSENGER_WITH_GIVEN_PHONE_NUMBER_ALREADY_EXISTS,
                    passenger.getPhoneNumber()));
        }

    }
}
