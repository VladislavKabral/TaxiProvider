package by.modsen.taxiprovider.ridesservice.controller.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDTO;
import by.modsen.taxiprovider.ridesservice.mapper.ride.PotentialRideMapper;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/rides")
public class RidesController {

    private final RidesService ridesService;

    private final PotentialRideMapper potentialRideMapper;

    @Autowired
    public RidesController(RidesService ridesService, PotentialRideMapper potentialRideMapper) {
        this.ridesService = ridesService;
        this.potentialRideMapper = potentialRideMapper;
    }

    @PostMapping("/potentialCost")
    public ResponseEntity<Double> getPotentialCost(@RequestBody @Valid PotentialRideDTO potentialRideDTO,
                                                    BindingResult bindingResult)
            throws ParseException, InterruptedException, IOException, DistanceCalculationException, EntityNotFoundException {

        PotentialRide potentialRide = potentialRideMapper.toEntity(potentialRideDTO);
        return new ResponseEntity<>(ridesService.calculatePotentialRideCost(potentialRide), HttpStatus.OK);
    }
}
