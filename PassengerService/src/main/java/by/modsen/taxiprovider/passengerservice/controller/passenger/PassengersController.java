package by.modsen.taxiprovider.passengerservice.controller.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.mapper.passenger.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.mapper.passenger.PassengerProfileMapper;
import by.modsen.taxiprovider.passengerservice.mapper.rating.RatingMapper;
import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.service.passenger.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.validation.PassengersValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
public class PassengersController {

    private final PassengersService passengersService;

    private final PassengerMapper passengerMapper;

    private final RatingMapper ratingMapper;

    private final PassengerProfileMapper passengerProfileMapper;

    private final PassengersValidator passengersValidator;

    @Autowired
    public PassengersController(PassengersService passengersService, PassengerMapper passengerMapper, RatingMapper ratingMapper, PassengerProfileMapper passengerProfileMapper, PassengersValidator passengersValidator) {
        this.passengersService = passengersService;
        this.passengerMapper = passengerMapper;
        this.ratingMapper = ratingMapper;
        this.passengerProfileMapper = passengerProfileMapper;
        this.passengersValidator = passengersValidator;
    }

    @GetMapping
    public ResponseEntity<List<PassengerDTO>> getPassengers() throws EntityNotFoundException {
        return new ResponseEntity<>(passengerMapper.toListDTO(passengersService.findAll()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/passenger", params = "id")
    public ResponseEntity<PassengerDTO> getPassengerById(@RequestParam long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengerMapper.toDTO(passengersService.findById(id)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/passenger", params = "email")
    public ResponseEntity<PassengerDTO> getPassengerByEmail(@RequestParam String email) throws EntityNotFoundException {
        return new ResponseEntity<>(passengerMapper.toDTO(passengersService.findByEmail(email)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getPassengerRating(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.getPassengerRating(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<PassengerProfileDTO> getPassengerProfile(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengerProfileMapper.toDTO(passengersService.getPassengerProfile(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> savePassenger(@RequestBody @Valid NewPassengerDTO passengerDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);
        passengersService.save(passenger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<HttpStatus> ratePassenger(@PathVariable long id,
                                                    @RequestBody @Valid RatingDTO ratingDTO,
                                                    BindingResult bindingResult) throws EntityNotFoundException, EntityValidateException {
        Rating rating = ratingMapper.toEntity(ratingDTO);
        handleBindingResult(bindingResult);
        passengersService.ratePassenger(id, rating);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPassenger(@PathVariable("id") long id,
                                                    @RequestBody @Valid PassengerDTO passengerDTO,
                                                    BindingResult bindingResult) throws EntityNotFoundException, EntityValidateException {

        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger.setId(id);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);
        passengersService.update(id, passenger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deactivatePassenger(@PathVariable("id") long id) throws EntityNotFoundException {
        passengersService.deactivate(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void handleBindingResult(BindingResult bindingResult) throws EntityValidateException {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();

            for (FieldError error: bindingResult.getFieldErrors()) {
                message.append(error.getDefaultMessage()).append(". ");
            }

            throw new EntityValidateException(message.toString());
        }
    }
}
