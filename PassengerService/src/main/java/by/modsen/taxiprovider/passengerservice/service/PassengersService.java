package by.modsen.taxiprovider.passengerservice.service;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.mapper.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.validation.PassengersValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PassengersService {

    private final PassengersRepository passengersRepository;

    private final PassengerMapper passengerMapper;

    private final PassengersValidator passengersValidator;

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    public List<PassengerDTO> findAll() throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findByStatusOrderByLastname("Active");

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers");
        }

        return passengerMapper.toListDTO(passengers);
    }

    public List<PassengerDTO> findPagePassengers(int index, int count) throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository
                .findAll(PageRequest.of(index, count, Sort.by("lastname"))).getContent();

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers on this page");
        }

        return passengerMapper.toListDTO(passengers.stream()
                .filter(passenger -> passenger.getStatus().equals("Active"))
                .collect(Collectors.toList()));
    }

    public PassengerDTO findById(long id) throws EntityNotFoundException {
        return passengerMapper.toDTO(passengersRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException("Passenger with id '" + id + "' wasn't found")));
    }

    @Transactional
    public void save(NewPassengerDTO passengerDTO, BindingResult bindingResult) throws EntityValidateException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);

        passenger.setRole("Passenger");
        passenger.setStatus("Active");

        passengersRepository.save(passenger);
    }

    @Transactional
    public void update(long id, PassengerDTO passengerDTO, BindingResult bindingResult) throws EntityNotFoundException,
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
    }

    @Transactional
    public void deactivate(long id) throws EntityNotFoundException {
        Passenger passenger = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Passenger with id '" + id + "' wasn't found"));

        passenger.setStatus("Inactive");

        passengersRepository.save(passenger);
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
