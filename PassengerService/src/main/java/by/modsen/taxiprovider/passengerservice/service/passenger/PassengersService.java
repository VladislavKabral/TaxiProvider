package by.modsen.taxiprovider.passengerservice.service.passenger;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDTO;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.PassengerRatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.passengerservice.dto.request.RideRequestDTO;
import by.modsen.taxiprovider.passengerservice.dto.response.RideDTO;
import by.modsen.taxiprovider.passengerservice.mapper.passenger.PassengerMapper;
import by.modsen.taxiprovider.passengerservice.mapper.passenger.PassengerProfileMapper;
import by.modsen.taxiprovider.passengerservice.mapper.rating.RatingMapper;
import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.passenger.PassengerProfile;
import by.modsen.taxiprovider.passengerservice.model.rating.PassengerRating;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.repository.passenger.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.service.ratings.RatingsService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.NotEnoughFreeDriversException;
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

    private final PassengerProfileMapper passengerProfileMapper;

    private final RatingMapper ratingMapper;

    private final RatingsService ratingsService;

    private final PassengersValidator passengersValidator;

    @Value("${rides-service-host-url}")
    private String RIDES_SERVICE_HOST_URL;

    private static final int COUNT_OF_RATINGS = 30;

    private static final int DEFAULT_RATING_VALUE = 5;

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

    public boolean isPassengerExists(long id) {
        return passengersRepository.existsById(id);
    }

    @Transactional
    public void save(NewPassengerDTO passengerDTO, BindingResult bindingResult) throws EntityValidateException {
        Passenger passenger = passengerMapper.toEntity(passengerDTO);
        passengersValidator.validate(passenger, bindingResult);
        handleBindingResult(bindingResult);

        passenger.setRole("Passenger");
        passenger.setStatus("Active");

        passengersRepository.save(passenger);

        for (int i = 0; i < COUNT_OF_RATINGS; i++) {
            Rating rating = new Rating();
            rating.setValue(DEFAULT_RATING_VALUE);
            rating.setPassenger(passenger);
            ratingsService.save(rating);
        }
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

    public PassengerRatingDTO getPassengerRating(long id) throws EntityNotFoundException {
        Passenger passenger = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                    .entityNotFoundException("Passenger with id '" + id + "' wasn't found"));

        return ratingMapper.toDTO(PassengerRating.builder()
                .value(ratingsService.calculatePassengerRating(passenger))
                .build());
    }

    @Transactional
    public void ratePassenger(long id, RatingDTO ratingDTO, BindingResult bindingResult) throws EntityNotFoundException,
            EntityValidateException {

        Passenger passenger = passengersRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException("Passenger with id '" + id + "' wasn't found"));

        Rating rating = ratingMapper.toEntity(ratingDTO);
        handleBindingResult(bindingResult);

        rating.setPassenger(passenger);

        ratingsService.save(rating);
    }

    public PassengerProfileDTO getPassengerProfile(long id) throws EntityNotFoundException {
        return passengerProfileMapper.toDTO(PassengerProfile.builder()
                .passenger(passengersRepository.findById(id)
                        .orElseThrow(EntityNotFoundException
                            .entityNotFoundException("Passenger with id '" + id + "' wasn't found")))
                .rating(ratingMapper.toEntity(getPassengerRating(id)))
                .build());
    }

    public Mono<RideRequestDTO> postRideRequest(RideRequestDTO rideRequestDTO, BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {

        handleBindingResult(bindingResult);

        long passengerId = rideRequestDTO.getPassengerId();
        if (!isPassengerExists(passengerId)) {
            throw new EntityNotFoundException("Passenger with id '" + passengerId + "' wasn't found");
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(RIDES_SERVICE_HOST_URL)
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE,
                        "Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .body(Mono.just(rideRequestDTO), RideRequestDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new NotEnoughFreeDriversException()))
                .bodyToMono(RideRequestDTO.class);
    }

    public List<RideDTO> getRidesHistory(long id) throws EntityNotFoundException {
        if (!isPassengerExists(id)) {
            throw new EntityNotFoundException("Passenger with id '" + id + "' wasn't found");
        }

        WebClient webClient = WebClient.builder()
                .baseUrl(RIDES_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri("/passenger/" + id)
                .retrieve()
                .bodyToFlux(RideDTO.class)
                .collect(Collectors.toList())
                .block();
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
