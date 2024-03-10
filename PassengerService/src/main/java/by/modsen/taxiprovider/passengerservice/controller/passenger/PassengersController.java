package by.modsen.taxiprovider.passengerservice.controller.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.PassengerRatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.request.RideRequestDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.RideDTO;
import by.modsen.taxiprovider.passengerservice.service.passenger.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
import reactor.core.publisher.Mono;

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

    @GetMapping("/{id}/rides")
    public ResponseEntity<List<RideDTO>> getRidesHistory(@PathVariable("id") long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.getRidesHistory(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> savePassenger(@RequestBody @Valid NewPassengerDTO passengerDTO,
                                                    BindingResult bindingResult) throws EntityValidateException {
        passengersService.save(passengerDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<HttpStatus> ratePassenger(@PathVariable long id,
                                                    @RequestBody @Valid RatingDTO ratingDTO,
                                                    BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        passengersService.ratePassenger(id, ratingDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/newRide")
    public ResponseEntity<Mono<RideRequestDTO>> postNewRideRequest(@PathVariable("id") long id,
                                                                   @RequestBody @Valid RideRequestDTO rideRequestDTO,
                                                                   BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        rideRequestDTO.setPassengerId(id);
        return new ResponseEntity<>(passengersService.postRideRequest(rideRequestDTO, bindingResult), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPassenger(@PathVariable("id") long id,
                                                    @RequestBody @Valid PassengerDTO passengerDTO,
                                                    BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        passengersService.update(id, passengerDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deactivatePassenger(@PathVariable("id") long id) throws EntityNotFoundException {
        passengersService.deactivate(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
