package by.modsen.taxiprovider.passengerservice.service;

import by.modsen.taxiprovider.passengerservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.request.PassengerRatingRequestDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDTO;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.passengerservice.util.exception.ExternalServiceUnavailableException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import by.modsen.taxiprovider.passengerservice.util.validation.PassengersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.util.Status.*;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengersService {

    private final PassengersRepository passengersRepository;

    private final PassengerMapper passengerMapper;

    private final PassengersValidator passengersValidator;

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String KAFKA_TOPIC_NAME = "RIDE";

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private static final int RETRY_DURATION_TIME = 5;

    public List<PassengerDTO> findAll() throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findByStatusOrderByLastname(PASSENGER_ACCOUNT_STATUS_ACTIVE);

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException(PASSENGERS_NOT_FOUND);
        }

        return passengerMapper.toListDTO(passengers);
    }

    public List<PassengerDTO> findSortedPassengers(String sortField) throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findAll(Sort.by(sortField));

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException(PASSENGERS_NOT_FOUND);
        }

        return passengerMapper.toListDTO(passengers);
    }

    public Page<PassengerDTO> findPagePassengers(int index, int count, String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        if ((index <= 0) || (count <= 0)) {
            throw new InvalidRequestDataException(INVALID_PAGE_REQUEST);
        }

        List<Passenger> passengers = passengersRepository
                .findAll(PageRequest.of(index - 1, count, Sort.by(sortField))).getContent()
                .stream()
                .filter(passenger -> passenger.getStatus().equals(PASSENGER_ACCOUNT_STATUS_ACTIVE))
                .toList();

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException(PASSENGERS_ON_PAGE_NOT_FOUND);
        }

        return new PageImpl<>(passengers.stream()
                .map(passengerMapper::toDTO)
                .toList());
    }

    public PassengerDTO findById(long id) throws EntityNotFoundException {
        return passengerMapper.toDTO(passengersRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id))));
    }

    @Transactional
    public PassengerResponseDTO save(NewPassengerDTO passengerDTO, BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);

        passenger.setRole(PASSENGER_ROLE_NAME);
        passenger.setStatus(PASSENGER_ACCOUNT_STATUS_ACTIVE);

        passengersRepository.save(passenger);

        Passenger createdPassenger = passengersRepository
                .findByEmail(passenger.getEmail())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_CREATED, passenger.getEmail())));

        initPassengerRating(createdPassenger.getId());

        return new PassengerResponseDTO(createdPassenger.getId());
    }

    @Transactional
    public PassengerResponseDTO update(long id, PassengerDTO passengerDTO, BindingResult bindingResult)
            throws EntityNotFoundException, EntityValidateException {
        Passenger passengerData = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));

        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger.setId(id);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);

        String firstname = passenger.getFirstname();
        String lastname = passenger.getLastname();
        String email = passenger.getEmail();
        String phoneNumber = passenger.getPhoneNumber();

        if (email != null) {
            passengerData.setEmail(email);
        }
        if (firstname != null) {
            passengerData.setFirstname(firstname);
        }
        if (lastname != null) {
            passengerData.setLastname(lastname);
        }
        if (phoneNumber != null) {
            passengerData.setPhoneNumber(phoneNumber);
        }

        passengersRepository.save(passengerData);

        return new PassengerResponseDTO(id);
    }

    @Transactional
    public PassengerResponseDTO deactivate(long id) throws EntityNotFoundException {
        Passenger passenger = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));

        passenger.setStatus(PASSENGER_ACCOUNT_STATUS_INACTIVE);

        passengersRepository.save(passenger);

        return new PassengerResponseDTO(id);
    }

    private void initPassengerRating(long passengerId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(RATINGS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.post()
                .uri("/init")
                .bodyValue(PassengerRatingRequestDTO.builder()
                        .taxiUserId(passengerId)
                        .role(PASSENGER_ROLE_NAME)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    RATINGS_SERVICE_HOST_URL));
                        }))
                .block();
    }

    private RatingDTO getPassengerRating(long passengerId) throws EntityNotFoundException {
        Passenger passenger = passengersRepository.findById(passengerId).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, passengerId)));

        WebClient webClient = WebClient.builder()
                .baseUrl(RATINGS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("taxiUserId", passenger.getId())
                        .queryParam("role", passenger.getRole())
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(errorResponseDTO -> new ExternalServiceRequestException(errorResponseDTO.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(RatingDTO.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    RATINGS_SERVICE_HOST_URL));
                        }))
                .block();
    }

    public PassengerProfileDTO getPassengerProfile(long id) throws EntityNotFoundException {
        PassengerDTO passenger = findById(id);

        return PassengerProfileDTO.builder()
                .passenger(passenger)
                .rating(getPassengerRating(id).getValue())
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
