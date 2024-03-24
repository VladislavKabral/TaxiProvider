package by.modsen.taxiprovider.passengerservice.controller.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDTO;
import by.modsen.taxiprovider.passengerservice.service.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/passengers")
@AllArgsConstructor
public class PassengersController {

    private final PassengersService passengersService;

    @GetMapping
    public ResponseEntity<List<PassengerDTO>> getPassengers() throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = {"sort"})
    public ResponseEntity<List<PassengerDTO>> getSortedPassengers(@RequestParam("sort") String sortField)
            throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findSortedPassengers(sortField), HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size", "sort"})
    public ResponseEntity<Page<PassengerDTO>> getPassengersPage(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        return new ResponseEntity<>(passengersService.findPagePassengers(page, size, sortField),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassengerDTO> getPassengerById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<PassengerProfileDTO> getPassengerProfile(@PathVariable long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.getPassengerProfile(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PassengerResponseDTO> savePassenger(@RequestBody @Valid NewPassengerDTO passengerDTO,
                                                              BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(passengersService.save(passengerDTO, bindingResult), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> editPassenger(@PathVariable("id") long id,
                                                    @RequestBody @Valid PassengerDTO passengerDTO,
                                                    BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        return new ResponseEntity<>(passengersService.update(id, passengerDTO, bindingResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> deactivatePassenger(@PathVariable("id") long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.deactivate(id), HttpStatus.OK);
    }
}
