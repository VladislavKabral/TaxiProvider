package by.modsen.taxiprovider.driverservice.service;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.driverservice.dto.response.DriverResponseDTO;
import by.modsen.taxiprovider.driverservice.mapper.DriverMapper;
import by.modsen.taxiprovider.driverservice.model.Driver;
import by.modsen.taxiprovider.driverservice.repository.DriversRepository;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import by.modsen.taxiprovider.driverservice.util.validation.DriversValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriversService {

    private final DriversRepository driversRepository;

    private final DriverMapper driverMapper;

    private final DriversValidator driversValidator;

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    public List<DriverDTO> findAll() throws EntityNotFoundException {
        List<Driver> drivers = driversRepository.findByAccountStatus("ACTIVE");

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any drivers");
        }

        return driverMapper.toListDTO(drivers);
    }

    public Page<DriverDTO> findPageDrivers(int index, int count, String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        if ((index <= 0) || (count <= 0)) {
            throw new InvalidRequestDataException("Number of page and count of elements can't be less than zero");
        }

        List<Driver> drivers = driversRepository
                .findAll(PageRequest.of(index - 1, count, Sort.by(sortField))).getContent()
                .stream()
                .filter(driver -> driver.getStatus().equals("ACTIVE"))
                .toList();

        if (drivers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any drivers on this page");
        }

        return new PageImpl<>(drivers.stream()
                .map(driverMapper::toDTO)
                .toList());
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
    public DriverResponseDTO save(NewDriverDTO driverDTO, BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        Driver driver = driverMapper.toEntity(driverDTO);
        driversValidator.validate(driver, bindingResult);
        handleBindingResult(bindingResult);

        driver.setRole("DRIVER");
        driver.setAccountStatus("ACTIVE");
        driver.setStatus("FREE");
        driver.setBalance(BigDecimal.ZERO);
        driversRepository.save(driver);

        return new DriverResponseDTO(driversRepository
                .findByEmail(driverDTO.getEmail())
                .orElseThrow(EntityNotFoundException.entityNotFoundException("Driver with email '" +
                        driver.getEmail() + "' wasn't created"))
                .getId());
    }

    @Transactional
    public DriverResponseDTO update(long id, DriverDTO driverDTO, BindingResult bindingResult) throws EntityNotFoundException,
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

        return new DriverResponseDTO(id);
    }

    @Transactional
    public DriverResponseDTO deactivate(long id) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Driver with id '" + id + "' wasn't found"));

        driver.setAccountStatus("INACTIVE");
        driversRepository.save(driver);

        return new DriverResponseDTO(id);
    }

    private RatingDTO getDriverRating(long driverId) throws EntityNotFoundException {
        Driver driver = driversRepository.findById(driverId).orElseThrow(EntityNotFoundException
                .entityNotFoundException("Driver with id '" + driverId + "' wasn't found"));

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
                        Mono.error(new EntityNotFoundException("Cannot find driver with id '" + driverId + "'")))
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
