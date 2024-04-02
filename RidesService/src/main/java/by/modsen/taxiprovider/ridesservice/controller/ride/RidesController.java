package by.modsen.taxiprovider.ridesservice.controller.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialCostDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.PotentialRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideListDto;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
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

@RestController
@RequestMapping("/rides")
@RequiredArgsConstructor
public class RidesController {

    private final RidesService ridesService;

    private final PromoCodesService promoCodesService;

    @GetMapping
    public ResponseEntity<RideListDto> getRides() {
        return new ResponseEntity<>(ridesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideDto> getRideById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/passenger/{id}")
    public ResponseEntity<RideListDto> getPassengerRides(@PathVariable("id") long id) {
        return new ResponseEntity<>(ridesService.findByPassengerId(id), HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity<RideListDto> getDriverRides(@PathVariable("id") long id) {
        return new ResponseEntity<>(ridesService.findByDriverId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RideResponseDto> saveRide(@RequestBody @Valid NewRideDto rideDTO, BindingResult bindingResult)
            throws EntityNotFoundException, IOException, ParseException, DistanceCalculationException,
            InterruptedException, EntityValidateException {

        PromoCodeDto promoCodeDTO = null;
        if (rideDTO.getPromoCode() != null) {
            promoCodeDTO = promoCodesService.findByValue(rideDTO.getPromoCode().getValue());
        }

        return new ResponseEntity<>(ridesService.save(rideDTO, promoCodeDTO, bindingResult), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<RideResponseDto> updateRide(@RequestBody @Valid RideDto rideDTO,
                                                      BindingResult bindingResult) throws EntityValidateException,
            EntityNotFoundException {
        return new ResponseEntity<>(ridesService.update(rideDTO, bindingResult), HttpStatus.OK);
    }

    @PatchMapping("/{passengerId}")
    public ResponseEntity<RideResponseDto> cancelRide(@PathVariable("passengerId") long passengerId)
            throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.cancel(passengerId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RideResponseDto> deactivateRide(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(ridesService.delete(id), HttpStatus.OK);
    }

    @PostMapping("/potentialCost")
    public ResponseEntity<PotentialCostDto> getPotentialCost(@RequestBody @Valid PotentialRideDto potentialRideDTO,
                                                             BindingResult bindingResult)
            throws ParseException, InterruptedException, IOException, DistanceCalculationException,
            EntityNotFoundException, EntityValidateException {

        return new ResponseEntity<>(new PotentialCostDto(ridesService
                .getPotentialRideCost(potentialRideDTO, bindingResult)),
                HttpStatus.OK);
    }

}
