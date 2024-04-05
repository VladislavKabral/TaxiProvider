package by.modsen.taxiprovider.passengerservice.controller.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerListDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengersPageDto;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDto;
import by.modsen.taxiprovider.passengerservice.service.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengersController {

    private final PassengersService passengersService;

    @GetMapping
    public ResponseEntity<PassengerListDto> getPassengers() {
        return new ResponseEntity<>(passengersService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size", "sort"})
    public ResponseEntity<PassengersPageDto> getPassengersPage(@RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                               @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                               @RequestParam(name = "sort", required = false, defaultValue = "lastname") String sortField)
            throws InvalidRequestDataException {
        return new ResponseEntity<>(passengersService.findPagePassengers(page, size, sortField),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDto> getPassengerById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<PassengerProfileDto> getPassengerProfile(@PathVariable long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.getPassengerProfile(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PassengerResponseDto> savePassenger(@RequestBody @Valid NewPassengerDto passengerDTO)
            throws EntityNotFoundException, EntityValidateException {
        return new ResponseEntity<>(passengersService.save(passengerDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponseDto> editPassenger(@PathVariable("id") long id,
                                                              @RequestBody @Valid PassengerDto passengerDTO)
            throws EntityNotFoundException, EntityValidateException {
        return new ResponseEntity<>(passengersService.update(id, passengerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponseDto> deactivatePassenger(@PathVariable("id") long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.deactivate(id), HttpStatus.OK);
    }
}
