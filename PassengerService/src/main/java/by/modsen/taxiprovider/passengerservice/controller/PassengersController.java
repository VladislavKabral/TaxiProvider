package by.modsen.taxiprovider.passengerservice.controller;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.passenger.PassengerProfile;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.service.passenger.PassengersService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/passengers")
public class PassengersController {

    private final ModelMapper modelMapper;

    private final PassengersService passengersService;

    @Autowired
    public PassengersController(ModelMapper modelMapper, PassengersService passengersService) {
        this.modelMapper = modelMapper;
        this.passengersService = passengersService;
    }

    @GetMapping
    public ResponseEntity<List<PassengerDTO>> getPassengers() throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.findAll()
                .stream()
                .map(this::convertToPassengerDTO)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/passenger", params = "id")
    public ResponseEntity<PassengerDTO> getPassengerById(@RequestParam long id) throws EntityNotFoundException {
        return new ResponseEntity<>(convertToPassengerDTO(passengersService.findById(id)),
                HttpStatus.OK);
    }

    @GetMapping(value = "/passenger", params = "email")
    public ResponseEntity<PassengerDTO> getPassengerByEmail(@RequestParam String email) throws EntityNotFoundException {
        return new ResponseEntity<>(convertToPassengerDTO(passengersService.findByEmail(email)),
                HttpStatus.OK);
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getPassengerRating(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(passengersService.getPassengerRating(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<PassengerProfileDTO> getPassengerProfile(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(convertToPassengerProfileDTO(passengersService.getPassengerProfile(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> savePassenger(@RequestBody @Valid NewPassengerDTO passengerDTO) {
        Passenger passenger = convertToPassenger(passengerDTO);

        passengersService.save(passenger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<HttpStatus> ratePassenger(@PathVariable long id, @RequestBody RatingDTO ratingDTO) throws EntityNotFoundException {
        Rating rating = convertToRating(ratingDTO);

        passengersService.ratePassenger(id, rating);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPassenger(@PathVariable("id") long id,
                                                    @RequestBody @Valid PassengerDTO passengerDTO) throws EntityNotFoundException {

        Passenger passenger = convertToPassenger(passengerDTO);

        passengersService.update(id, passenger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deactivatePassenger(@PathVariable("id") long id) throws EntityNotFoundException {
        passengersService.deactivate(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private PassengerDTO convertToPassengerDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    private Passenger convertToPassenger(PassengerDTO passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }

    private Passenger convertToPassenger(NewPassengerDTO passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }

    private Rating convertToRating(RatingDTO ratingDTO) {
        return modelMapper.map(ratingDTO, Rating.class);
    }

    private PassengerProfileDTO convertToPassengerProfileDTO(PassengerProfile passengerProfile) {
        return modelMapper.map(passengerProfile, PassengerProfileDTO.class);
    }
}
