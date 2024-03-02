package by.modsen.taxiprovider.passengerservice.controller.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.PassengerRatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.service.passenger.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.validation.PassengersValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengersController {

    private final PassengersService passengersService;

    private final PassengersValidator passengersValidator;

    @GetMapping
    public ResponseEntity<List<PassengerDTO>> getPassengers() throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<PassengerDTO>> getPassengersPage(@RequestParam("page") int page,
                                                                @RequestParam("size") int size)
            throws EntityNotFoundException {

        return new ResponseEntity<>(passengersService.findPagePassengers(page - 1,
                size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<PassengerRatingDTO> getPassengerRating(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.getPassengerRating(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<PassengerProfileDTO> getPassengerProfile(@PathVariable long id)
            throws EntityNotFoundException {

        return new ResponseEntity<>(passengersService.getPassengerProfile(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> savePassenger(@RequestBody @Valid NewPassengerDTO passengerDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {

        passengersValidator.validate(passengerDTO, bindingResult);
        handleBindingResult(bindingResult);
        passengersService.save(passengerDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<HttpStatus> ratePassenger(@PathVariable long id,
                                                    @RequestBody @Valid RatingDTO ratingDTO,
                                                    BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {

        handleBindingResult(bindingResult);
        passengersService.ratePassenger(id, ratingDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPassenger(@PathVariable("id") long id,
                                                    @RequestBody @Valid PassengerDTO passengerDTO,
                                                    BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {

        passengersValidator.validate(passengerDTO, bindingResult);
        handleBindingResult(bindingResult);
        passengersService.update(id, passengerDTO);

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
