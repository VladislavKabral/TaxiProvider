package by.modsen.taxiprovider.ridesservice.controller.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialCostDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import by.modsen.taxiprovider.ridesservice.mapper.ride.PotentialRideMapper;
import by.modsen.taxiprovider.ridesservice.mapper.ride.RideMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rides")
@AllArgsConstructor
public class RidesController {

    private final RidesService ridesService;

    private final PotentialRideMapper potentialRideMapper;

    private final PromoCodesService promoCodesService;

    private final RideMapper rideMapper;

    @GetMapping
    public ResponseEntity<List<RideDTO>> getRides() throws EntityNotFoundException {
        return new ResponseEntity<>(rideMapper.toListDTO(ridesService.findAll()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> saveRide(@RequestBody @Valid RideDTO rideDTO, BindingResult bindingResult) throws EntityNotFoundException, IOException, ParseException, DistanceCalculationException, InterruptedException {
        PromoCode promoCode = null;
        if (rideDTO.getPromoCode() != null) {
            promoCode = promoCodesService.findByValue(rideDTO.getPromoCode().getValue());
        }

        ridesService.save(rideMapper.toEntity(rideDTO), promoCode);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/potentialCost")
    public ResponseEntity<PotentialCostDTO> getPotentialCost(@RequestBody @Valid PotentialRideDTO potentialRideDTO,
                                                             BindingResult bindingResult)
            throws ParseException, InterruptedException, IOException, DistanceCalculationException, EntityNotFoundException {

        PotentialRide potentialRide = potentialRideMapper.toEntity(potentialRideDTO);
        return new ResponseEntity<>(new PotentialCostDTO(ridesService.calculatePotentialRideCost(potentialRide)),
                HttpStatus.OK);
    }
}
