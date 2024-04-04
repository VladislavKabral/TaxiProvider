package by.modsen.taxiprovider.driverservice.controller.driver;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriversPageDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.service.DriversService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriversController {

    private final DriversService driversService;

    @GetMapping
    public ResponseEntity<DriverListDto> getDrivers() {
        return new ResponseEntity<>(driversService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size", "sort"})
    public ResponseEntity<DriversPageDto> getDriversPage(@RequestParam("page") int page,
                                                         @RequestParam("size") int size,
                                                         @RequestParam("sort") String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        return new ResponseEntity<>(driversService.findPageDrivers(page, size, sortField), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getDriverById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<DriverProfileDto> getDriverProfile(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.getDriverProfile(id), HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<DriverListDto> getFreeDrivers() {
        return new ResponseEntity<>(driversService.findFreeDrivers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DriverResponseDto> saveDriver(@RequestBody @Valid NewDriverDto driverDTO,
                                                        BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(driversService.save(driverDTO, bindingResult), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<DriverResponseDto> editDriver(@PathVariable("id") long id,
                                                        @RequestBody @Valid DriverDto driverDTO,
                                                        BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        return new ResponseEntity<>(driversService.update(id, driverDTO, bindingResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DriverResponseDto> deactivateDriver(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.deactivate(id), HttpStatus.OK);
    }
}
