package by.modsen.taxiprovider.driverservice.service;

import by.modsen.taxiprovider.driverservice.client.RatingHttpClient;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDto;
import by.modsen.taxiprovider.driverservice.mapper.DriverMapper;
import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import by.modsen.taxiprovider.driverservice.util.validation.DriversValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static by.modsen.taxiprovider.driverservice.util.Status.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@EnableKafka
@RequiredArgsConstructor
public class DriversService {

    private final DriversRepository driversRepository;

    private final DriverMapper driverMapper;

    private final DriversValidator driversValidator;

    private final RatingHttpClient ratingHttpClient;

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String KAFKA_TOPIC_NAME = "RIDE";

    public List<DriverDto> findAll() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByAccountStatus(DRIVER_ACCOUNT_STATUS_ACTIVE);

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException(DRIVERS_NOT_FOUND);
        }

        return driverMapper.toListDTO(drivers);
    }

    public Page<DriverDto> findPageDrivers(int index, int count, String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        if ((index <= 0) || (count <= 0)) {
            throw new InvalidRequestDataException(INVALID_PAGE_REQUEST);
        }

        List<Driver> drivers = driversRepository
                .findAll(PageRequest.of(index - 1, count, Sort.by(sortField))).getContent()
                .stream()
                .filter(driver -> driver.getStatus().equals(DRIVER_ACCOUNT_STATUS_ACTIVE))
                .toList();

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException(DRIVERS_ON_PAGE_NOT_FOUND);
        }

        return new PageImpl<>(drivers.stream()
                .map(driverMapper::toDTO)
                .toList());
    }

    public DriverDto findById(long id) throws EntityNotFoundException {
        return driverMapper.toDTO(driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id))));
    }

    public List<DriverDto> findFreeDrivers() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByStatus(DRIVER_STATUS_FREE);

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException(FREE_DRIVERS_NOT_FOUND);
        }

        return driverMapper.toListDTO(drivers);
    }

    @Transactional
    public DriverResponseDto save(NewDriverDto driverDTO, BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        Driver driver = driverMapper.toEntity(driverDTO);
        driversValidator.validate(driver, bindingResult);
        handleBindingResult(bindingResult);

        driver.setRole(DRIVER_ROLE_NAME);
        driver.setAccountStatus(DRIVER_ACCOUNT_STATUS_ACTIVE);
        driver.setStatus(DRIVER_STATUS_FREE);
        driver.setBalance(BigDecimal.ZERO);
        driversRepository.save(driver);

        Driver createdDriver = driversRepository
                .findByEmail(driverDTO.getEmail())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVERS_NOT_CREATED, driver.getEmail())));

        ratingHttpClient.initDriverRating(createdDriver.getId());

        return new DriverResponseDto(createdDriver.getId());
    }

    @Transactional
    public DriverResponseDto update(long id, DriverDto driverDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id)));

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
            if ((!status.equals(DRIVER_STATUS_FREE)) && (!status.equals(DRIVER_STATUS_TAKEN))) {
                throw new EntityValidateException(INVALID_DRIVER_STATUS);
            } else {
                driver.setStatus(status);
            }
        }
        if (balance != null) {
            driver.setBalance(balance);
        }

        driver.setId(id);
        driversRepository.save(driver);

        return new DriverResponseDto(id);
    }

    @Transactional
    public DriverResponseDto deactivate(long id) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id)));

        driver.setAccountStatus(DRIVER_ACCOUNT_STATUS_INACTIVE);
        driversRepository.save(driver);

        return new DriverResponseDto(id);
    }

    public DriverProfileDto getDriverProfile(long id) throws EntityNotFoundException {
        DriverDto driver = findById(id);

        return DriverProfileDto.builder()
                .driver(driver)
                .rating(ratingHttpClient.getDriverRating(id).getValue())
                .build();
    }

    @KafkaListener(topics = KAFKA_TOPIC_NAME)
    private void messageListener(String message) {
        //TODO: change to log
        System.out.println(message);
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
