package by.modsen.taxiprovider.driverservice.service;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriversPageDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDTO;
import by.modsen.taxiprovider.driverservice.mapper.DriverMapper;
import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static by.modsen.taxiprovider.driverservice.util.Status.*;
import static by.modsen.taxiprovider.driverservice.util.Message.*;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriversService {

    private final DriversRepository driversRepository;

    private final DriverMapper driverMapper;

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    public List<DriverDTO> findAll() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByAccountStatus(DRIVER_ACCOUNT_STATUS_ACTIVE);

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException(DRIVERS_NOT_FOUND);
        }

        return driverMapper.toListDTO(drivers);
    }

    public DriversPageDto findPageDrivers(int index, int count, String sortField)
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

        return DriversPageDto.builder()
                .content(driverMapper.toListDTO(drivers))
                .page(index)
                .size(count)
                .build();
    }

    public DriverDTO findById(long id) throws EntityNotFoundException {
        return driverMapper.toDTO(driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id))));
    }

    public List<DriverDTO> findFreeDrivers() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByStatus(DRIVER_STATUS_FREE);

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException(FREE_DRIVERS_NOT_FOUND);
        }

        return driverMapper.toListDTO(drivers);
    }

    @Transactional
    public DriverResponseDTO save(NewDriverDTO driverDTO) throws EntityNotFoundException {
        Driver driver = driverMapper.toEntity(driverDTO);

        driver.setRole(DRIVER_ROLE_NAME);
        driver.setAccountStatus(DRIVER_ACCOUNT_STATUS_ACTIVE);
        driver.setStatus(DRIVER_STATUS_FREE);
        driver.setBalance(BigDecimal.ZERO);
        driversRepository.save(driver);

        return new DriverResponseDTO(driversRepository
                .findByEmail(driverDTO.getEmail())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVERS_NOT_CREATED, driver.getEmail())))
                .getId());
    }

    @Transactional
    public DriverResponseDTO update(long id, DriverDTO driverDTO) throws EntityNotFoundException,
            EntityValidateException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id)));

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

        return new DriverResponseDTO(id);
    }

    @Transactional
    public DriverResponseDTO deactivate(long id) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(DRIVER_NOT_FOUND, id)));

        driver.setAccountStatus(DRIVER_ACCOUNT_STATUS_INACTIVE);
        driversRepository.save(driver);

        return new DriverResponseDTO(id);
    }

    private RatingDTO getDriverRating(long driverId) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(driverId).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(DRIVER_NOT_FOUND, driverId)));

        WebClient webClient = WebClient.builder()
                .baseUrl(RATINGS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("taxiUserId", driver.getId())
                        .queryParam("role", driver.getRole())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new EntityNotFoundException(String.format(DRIVER_NOT_FOUND, driver.getId()))))
                .bodyToMono(RatingDTO.class)
                .block();
    }

    public DriverProfileDTO getDriverProfile(long id) throws EntityNotFoundException {
        DriverDTO driver = findById(id);

        return DriverProfileDTO.builder()
                .driver(driver)
                .rating(getDriverRating(id).getValue())
                .build();
    }
}
