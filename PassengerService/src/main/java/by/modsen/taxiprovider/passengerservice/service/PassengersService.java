package by.modsen.taxiprovider.passengerservice.service;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengersPageDto;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDTO;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
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
import static by.modsen.taxiprovider.passengerservice.util.Message.*;
import static by.modsen.taxiprovider.passengerservice.util.Status.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengersService {

    private final PassengersRepository passengersRepository;

    private final PassengerMapper passengerMapper;

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    public List<PassengerDTO> findAll() throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findByStatusOrderByLastname(PASSENGER_ACCOUNT_STATUS_ACTIVE);

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException(PASSENGERS_NOT_FOUND);
        }

        return passengerMapper.toListDTO(passengers);
    }

    public PassengersPageDto findPagePassengers(int index, int count, String sortField)
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

        return PassengersPageDto.builder()
                .content(passengerMapper.toListDTO(passengers))
                .page(index)
                .size(count)
                .build();
    }

    public PassengerDTO findById(long id) throws EntityNotFoundException {
        return passengerMapper.toDTO(passengersRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id))));
    }

    @Transactional
    public PassengerResponseDTO save(NewPassengerDTO passengerDTO) throws EntityNotFoundException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);

        passenger.setRole(PASSENGER_ROLE_NAME);
        passenger.setStatus(PASSENGER_ACCOUNT_STATUS_ACTIVE);

        passengersRepository.save(passenger);

        return new PassengerResponseDTO(passengersRepository
                .findByEmail(passenger.getEmail())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_CREATED, passenger.getEmail())))
                .getId());
    }

    @Transactional
    public PassengerResponseDTO update(long id, PassengerDTO passengerDTO)
            throws EntityNotFoundException {
        Passenger passengerData = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PASSENGER_NOT_FOUND, id)));

        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passenger.setId(id);

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
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new EntityNotFoundException(String.format(PASSENGER_NOT_FOUND, passenger.getId()))))
                .bodyToMono(RatingDTO.class)
                .block();
    }

    public PassengerProfileDTO getPassengerProfile(long id) throws EntityNotFoundException {
        PassengerDTO passenger = findById(id);

        return PassengerProfileDTO.builder()
                .passenger(passenger)
                .rating(getPassengerRating(id).getValue())
                .build();
    }
}
