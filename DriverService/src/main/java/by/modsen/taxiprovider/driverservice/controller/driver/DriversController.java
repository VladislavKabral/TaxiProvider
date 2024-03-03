package by.modsen.taxiprovider.driverservice.controller.driver;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.DriverRatingDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.driverservice.service.driver.DriversService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.validation.DriversValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriversController {

    private final DriversService driversService;

    private final DriversValidator driversValidator;

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getDrivers() throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size"})
    public ResponseEntity<List<DriverDTO>> getDriversPage(@RequestParam("page") int page,
                                                          @RequestParam("size") int size)
            throws EntityNotFoundException {

        return new ResponseEntity<>(driversService.findPageDrivers(page, size), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/rating")
    public ResponseEntity<DriverRatingDTO> getDriverRating(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.getDriverRating(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<DriverProfileDTO> getDriverProfile(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.getDriverProfile(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> saveDriver(@RequestBody @Valid NewDriverDTO driverDTO,
                                                 BindingResult bindingResult) throws EntityValidateException {

        driversValidator.validate(driverDTO, bindingResult);
        handleBindingResult(bindingResult);
        driversService.save(driverDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<HttpStatus> rateDriver(@PathVariable("id") long id,
                                                 @RequestBody @Valid RatingDTO ratingDTO,
                                                 BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {

        handleBindingResult(bindingResult);
        driversService.rateDriver(id, ratingDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> editDriver(@PathVariable("id") long id,
                                                 @RequestBody @Valid DriverDTO driverDTO,
                                                 BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {

        driversValidator.validate(driverDTO, bindingResult);
        handleBindingResult(bindingResult);
        driversService.update(id, driverDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deactivateDriver(@PathVariable("id") long id) throws EntityNotFoundException {
        driversService.deactivate(id);

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
