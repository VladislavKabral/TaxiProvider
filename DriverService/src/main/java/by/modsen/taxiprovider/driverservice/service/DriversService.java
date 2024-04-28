package by.modsen.taxiprovider.driverservice.service;

import by.modsen.taxiprovider.driverservice.client.RatingHttpClient;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriversPageDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.modsen.taxiprovider.driverservice.util.Status.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@EnableKafka
@RequiredArgsConstructor
@Slf4j
public class DriversService {

    private final DriversRepository driversRepository;

    private final DriverMapper driverMapper;

    private final RatingHttpClient ratingHttpClient;

    private final DriversValidator driversValidator;

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String KAFKA_TOPIC_NAME = "RIDE";

    public DriverListDto findAll() {
        log.info(FIND_ALL_DRIVERS);
        List<Driver> drivers = driversRepository.findByAccountStatus(DRIVER_ACCOUNT_STATUS_ACTIVE);

        return DriverListDto.builder()
                .content(driverMapper.toListDto(drivers))
                .build();
    }

    public DriversPageDto findPageDrivers(int index, int count, String sortField)
            throws InvalidRequestDataException {
        if ((index <= 0) || (count <= 0)) {
            throw new InvalidRequestDataException(INVALID_PAGE_REQUEST);
        }

        log.info(FIND_DRIVERS);
        List<Driver> drivers = driversRepository
                .findAll(PageRequest.of(index - 1, count, Sort.by(sortField))).getContent()
                .stream()
                .filter(driver -> driver.getAccountStatus().equals(DRIVER_ACCOUNT_STATUS_ACTIVE))
                .toList();

        return DriversPageDto.builder()
                .page(index)
                .size(count)
                .content(driverMapper.toListDto(drivers))
                .build();
    }

    public DriverDto findById(long id) throws EntityNotFoundException {
        log.info(String.format(FIND_DRIVER_BY_ID, id));
        return driverMapper.toDto(findDriver(id));
    }

    private Driver findDriver(long id) throws EntityNotFoundException {
        return driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id)));
    }

    public DriverListDto findFreeDrivers() {
        log.info(FIND_FREE_DRIVERS);
        List<Driver> drivers = driversRepository.findByStatus(DRIVER_STATUS_FREE);

        return DriverListDto.builder()
                .content(driverMapper.toListDto(drivers))
                .build();
    }

    @Transactional
    public DriverResponseDto save(NewDriverDto driverDTO) throws EntityNotFoundException, EntityValidateException {
        log.info(SAVE_NEW_DRIVER);
        Driver driver = driverMapper.toEntity(driverDTO);

        driversValidator.validate(driver);

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

        log.info(String.format(DRIVER_WAS_SAVED, createdDriver.getLastname(), createdDriver.getFirstname()));
        return new DriverResponseDto(createdDriver.getId());
    }

    @Transactional
    public DriverResponseDto update(long id, DriverDto driverDTO) throws EntityNotFoundException,
            EntityValidateException {
        log.info(UPDATE_DRIVER);
        Driver driver = findDriver(id);

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

        driversValidator.validate(driver);

        driver.setId(id);
        driversRepository.save(driver);

        log.info(String.format(DRIVER_WAS_UPDATED, driver.getId()));
        return new DriverResponseDto(id);
    }

    @Transactional
    public DriverResponseDto deactivate(long id) throws EntityNotFoundException {
        log.info(DEACTIVATE_DRIVER);
        Driver driver = findDriver(id);

        driver.setAccountStatus(DRIVER_ACCOUNT_STATUS_INACTIVE);
        driversRepository.save(driver);

        log.info(String.format(DRIVER_WAS_DEACTIVATED, driver.getId()));
        return new DriverResponseDto(id);
    }

    public DriverProfileDto getDriverProfile(long id) throws EntityNotFoundException {
        log.info(String.format(FIND_DRIVER_PROFILE, id));
        DriverDto driver = findById(id);

        return DriverProfileDto.builder()
                .driver(driver)
                .rating(ratingHttpClient.getDriverRating(id).getValue())
                .build();
    }

    @KafkaListener(topics = KAFKA_TOPIC_NAME)
    private void messageListener(String message) {
        log.info(message);
    }
}
