package by.modsen.taxiprovider.passengerservice.util.validation;

import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.mapper.passenger.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.service.passenger.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class PassengersValidator implements Validator {

    private final PassengersService passengersService;

    private final PassengerMapper passengerMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return PassengerDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Passenger passenger = passengerMapper.toEntity((PassengerDTO) target);

        try {
            Passenger existingPassenger = passengersService.findByEmail(passenger.getEmail());
            if ((existingPassenger != null) && (existingPassenger.getId() != passenger.getId())) {
                errors.rejectValue("email", "", "A passenger with email '" +
                        passenger.getEmail() + "' already exists");
            }
        } catch (EntityNotFoundException e) {
            //TODO: add log
        }

        try {
            Passenger existingPassenger = passengersService.findByPhoneNumber(passenger.getPhoneNumber());
            if ((existingPassenger != null) && (existingPassenger.getId() != passenger.getId())) {
                errors.rejectValue("phoneNumber", "", "A passenger with phone number '" +
                        passenger.getPhoneNumber() + "' already exists");
            }
        } catch (EntityNotFoundException e) {
            //TODO: add log
        }
    }
}
