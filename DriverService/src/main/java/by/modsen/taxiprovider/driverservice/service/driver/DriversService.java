package by.modsen.taxiprovider.driverservice.service.driver;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.DriverRatingDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.driverservice.mapper.driver.DriverMapper;
import by.modsen.taxiprovider.driverservice.mapper.rating.RatingMapper;
import by.modsen.taxiprovider.driverservice.model.driver.Driver;
import by.modsen.taxiprovider.driverservice.model.rating.DriverRating;
import by.modsen.taxiprovider.driverservice.model.rating.Rating;
import by.modsen.taxiprovider.driverservice.repository.driver.DriversRepository;
import by.modsen.taxiprovider.driverservice.service.rating.RatingsService;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.validation.DriversValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriversService {

    private final DriversRepository driversRepository;

    private final DriverMapper driverMapper;

    private final DriversValidator driversValidator;

    private final RatingsService ratingsService;

    private final RatingMapper ratingMapper;

    private static final int COUNT_OF_RATINGS = 30;

    private static final int DEFAULT_RATING_VALUE = 5;

    public List<DriverDTO> findAll() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByAccountStatus("ACTIVE");

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any drivers");
        }

        return driverMapper.toListDTO(drivers);
    }

    public List<DriverDTO> findPageDrivers(int index, int count) throws EntityNotFoundException {
        List<Driver> drivers = driversRepository
                .findAll(PageRequest.of(index, count, Sort.by("lastname"))).getContent();

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any drivers on this page");
        }

        return driverMapper.toListDTO(drivers);
    }

    public DriverDTO findById(long id) throws EntityNotFoundException {
        return driverMapper.toDTO(driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Driver with id '" + id + "' wasn't found")));
    }

    public List<DriverDTO> findFreeDrivers() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByStatus("FREE");

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any free drivers");
        }

        return driverMapper.toListDTO(drivers);
    }

    @Transactional
    public void save(NewDriverDTO driverDTO, BindingResult bindingResult) throws EntityValidateException {
        Driver driver = driverMapper.toEntity(driverDTO);
        driversValidator.validate(driver, bindingResult);
        handleBindingResult(bindingResult);

        driver.setRole("DRIVER");
        driver.setAccountStatus("ACTIVE");
        driver.setStatus("FREE");
        driver.setBalance(BigDecimal.ZERO);
        driversRepository.save(driver);

        for (int i = 0; i < COUNT_OF_RATINGS; i++) {
            Rating rating = new Rating();
            rating.setValue(DEFAULT_RATING_VALUE);
            rating.setDriver(driver);
            ratingsService.save(rating);
        }
    }

    @Transactional
    public void update(long id, DriverDTO driverDTO, BindingResult bindingResult) throws EntityNotFoundException,
            EntityValidateException {

        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Driver with id '" + id + "' wasn't found"));

        driversValidator.validate(driver, bindingResult);
        handleBindingResult(bindingResult);

        String firstname = driverDTO.getFirstname();
        String lastname = driverDTO.getLastname();
        String email = driverDTO.getEmail();
        String phoneNumber = driverDTO.getPhoneNumber();
        String status = driverDTO.getStatus();
        BigDecimal balance = driverDTO.getBalance();

        if (email != null) {
            driver.setEmail(email);
        }
        if (firstname != null) {
            driver.setFirstname(firstname);
        }
        if (lastname != null) {
            driver.setLastname(lastname);
        }
        if (phoneNumber != null) {
            driver.setPhoneNumber(phoneNumber);
        }
        if (status != null) {
            if ((!status.equals("FREE")) && (!status.equals("TAKEN"))) {
                throw new EntityValidateException("Invalid ride status for driver");
            } else {
                driver.setStatus(status);
            }
        }
        if (balance != null) {
            driver.setBalance(balance);
        }

        driver.setId(id);
        driversRepository.save(driver);
    }

    @Transactional
    public void deactivate(long id) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Driver with id '" + id + "' wasn't found"));

        driver.setAccountStatus("INACTIVE");
        driversRepository.save(driver);
    }

    public DriverRatingDTO getDriverRating(long id) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Driver with id '" + id + "' wasn't found"));

        return ratingMapper.toDTO(new DriverRating(ratingsService.calculateDriverRating(driver)));
    }

    @Transactional
    public void rateDriver(long id, RatingDTO ratingDTO, BindingResult bindingResult) throws EntityNotFoundException,
            EntityValidateException {

        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Driver with id '" + id + "' wasn't found"));

        handleBindingResult(bindingResult);
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating.setDriver(driver);

        ratingsService.save(rating);
    }

    public DriverProfileDTO getDriverProfile(long id) throws EntityNotFoundException {
        DriverDTO driver = findById(id);
        DriverRatingDTO driverRating = getDriverRating(id);

        return DriverProfileDTO.builder()
                .driver(driver)
                .rating(driverRating)
                .build();
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
