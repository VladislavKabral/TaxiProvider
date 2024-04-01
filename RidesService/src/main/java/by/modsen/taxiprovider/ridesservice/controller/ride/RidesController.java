package by.modsen.taxiprovider.ridesservice.controller.ride;

import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialCostDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RidesController {

    private final RidesService ridesService;

    @GetMapping
    public ResponseEntity<List<RideDTO>> getRides() throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDTO> getRideById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<List<RideDTO>> getPassengerRides(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.findByPassengerId(id), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<List<RideDTO>> getDriverRides(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.findByDriverId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RideResponseDTO> saveRide(@RequestBody @Valid NewRideDTO rideDTO, BindingResult bindingResult)
            throws EntityNotFoundException, IOException, ParseException, DistanceCalculationException,
            InterruptedException, EntityValidateException {
        return new ResponseEntity<>(ridesService.save(rideDTO, bindingResult), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<RideResponseDTO> updateRide(@RequestBody @Valid RideDTO rideDTO,
                                                 BindingResult bindingResult) throws EntityValidateException,
            EntityNotFoundException {
        return new ResponseEntity<>(ridesService.update(rideDTO, bindingResult), HttpStatus.OK);
    }

    @PatchMapping("/{passengerId}")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable("passengerId") long passengerId)
            throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.cancel(passengerId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RideResponseDTO> deactivateRide(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.delete(id), HttpStatus.OK);
    }

    @PostMapping("/potentialCost")
    public ResponseEntity<PotentialCostDTO> getPotentialCost(@RequestBody @Valid PotentialRideDTO potentialRideDTO,
                                                             BindingResult bindingResult)
            throws ParseException, InterruptedException, IOException, DistanceCalculationException,
            EntityNotFoundException, EntityValidateException {

        return new ResponseEntity<>(new PotentialCostDTO(ridesService
                .getPotentialRideCost(potentialRideDTO, bindingResult)),
                HttpStatus.OK);
    }

}
