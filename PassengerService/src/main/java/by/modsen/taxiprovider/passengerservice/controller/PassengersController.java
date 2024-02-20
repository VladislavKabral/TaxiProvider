package by.modsen.taxiprovider.passengerservice.controller;

import by.modsen.taxiprovider.passengerservice.dto.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.service.PassengersService;
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

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editPassenger(@PathVariable("id") long id,
                                                    @RequestBody @Valid PassengerDTO passengerDTO) {

        Passenger passenger = convertToPassenger(passengerDTO);

        passengersService.update(id, passenger);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private PassengerDTO convertToPassengerDTO(Passenger passenger) {
        return modelMapper.map(passenger, PassengerDTO.class);
    }

    private Passenger convertToPassenger(PassengerDTO passengerDTO) {
        return modelMapper.map(passengerDTO, Passenger.class);
    }
}
