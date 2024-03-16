package by.modsen.taxiprovider.driverservice.controller.driver;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDTO;
import by.modsen.taxiprovider.driverservice.service.DriversService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriversController {

    private final DriversService driversService;

    @GetMapping
    public ResponseEntity<List<DriverDTO>> getDrivers() throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.findAll(), HttpStatus.OK);
    }

    @GetMapping(params = {"page", "size", "sort"})
    public ResponseEntity<Page<DriverDTO>> getDriversPage(@RequestParam("page") int page,
                                                          @RequestParam("size") int size,
                                                          @RequestParam("sort") String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        return new ResponseEntity<>(driversService.findPageDrivers(page, size, sortField), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDTO> getDriverById(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<DriverProfileDTO> getDriverProfile(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.getDriverProfile(id), HttpStatus.OK);
    }

    @GetMapping("/free")
    public ResponseEntity<List<DriverDTO>> getFreeDrivers() throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.findFreeDrivers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DriverResponseDTO> saveDriver(@RequestBody @Valid NewDriverDTO driverDTO,
                                                        BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(driversService.save(driverDTO, bindingResult), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<DriverResponseDTO> editDriver(@PathVariable("id") long id,
                                                 @RequestBody @Valid DriverDTO driverDTO,
                                                 BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        return new ResponseEntity<>(driversService.update(id, driverDTO, bindingResult), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> deactivateDriver(@PathVariable("id") long id) throws EntityNotFoundException {
        return new ResponseEntity<>(driversService.deactivate(id), HttpStatus.OK);
    }
}
