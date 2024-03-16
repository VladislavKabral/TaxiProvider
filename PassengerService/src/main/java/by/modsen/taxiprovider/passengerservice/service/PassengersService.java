package by.modsen.taxiprovider.passengerservice.service;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.PassengerResponseDTO;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengersService {

    private final PassengersRepository passengersRepository;

    private final PassengerMapper passengerMapper;

    private final PassengersValidator passengersValidator;

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    public List<PassengerDTO> findAll() throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findByStatusOrderByLastname("ACTIVE");

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers");
        }

        return passengerMapper.toListDTO(passengers);
    }

    public Page<PassengerDTO> findPagePassengers(int index, int count, String sortField)
            throws EntityNotFoundException, InvalidRequestDataException {
        if ((index <= 0) || (count <= 0)) {
            throw new InvalidRequestDataException("Number of page and count of elements can't be less than zero");
        }

        List<Passenger> passengers = passengersRepository
                .findAll(PageRequest.of(index - 1, count, Sort.by(sortField))).getContent()
                .stream()
                .filter(passenger -> passenger.getStatus().equals("ACTIVE"))
                .toList();

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers on this page");
        }

        return new PageImpl<>(passengers.stream()
                .map(passengerMapper::toDTO)
                .toList());
    }

    public PassengerDTO findById(long id) throws EntityNotFoundException {
        return passengerMapper.toDTO(passengersRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException("Passenger with id '" + id + "' wasn't found")));
    }

    @Transactional
    public PassengerResponseDTO save(NewPassengerDTO passengerDTO, BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);

        passenger.setRole("PASSENGER");
        passenger.setStatus("ACTIVE");

        passengersRepository.save(passenger);

        return new PassengerResponseDTO(passengersRepository
                .findByEmail(passenger.getEmail())
                .orElseThrow(EntityNotFoundException.entityNotFoundException("Passenger with email '" +
                        passenger.getEmail() + "' wasn't created"))
                .getId());
    }

    @Transactional
    public PassengerResponseDTO update(long id, PassengerDTO passengerDTO, BindingResult bindingResult) throws EntityNotFoundException,
            EntityValidateException {

        Passenger passengerData = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Passenger with id '" + id + "' wasn't found"));

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
                        .entityNotFoundException("Passenger with id '" + id + "' wasn't found"));

        passenger.setStatus("INACTIVE");

        passengersRepository.save(passenger);

        return new PassengerResponseDTO(id);
    }

    private RatingDTO getPassengerRating(long passengerId) throws EntityNotFoundException {
        Passenger passenger = passengersRepository.findById(passengerId).orElseThrow(EntityNotFoundException
                .entityNotFoundException("Passenger with id '" + passengerId + "' wasn't found"));

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
                        Mono.error(new EntityNotFoundException("Cannot find passenger with id '" + passengerId + "'")))
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
