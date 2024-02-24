package by.modsen.taxiprovider.passengerservice.service.passenger;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.passenger.PassengerProfile;
import by.modsen.taxiprovider.passengerservice.model.rating.PassengerRating;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.repository.passenger.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.service.ratings.RatingsService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PassengersService {
    private final PassengersRepository passengersRepository;

    private final RatingsService ratingsService;

    private static final int COUNT_OF_RATINGS = 30;

    private static final int DEFAULT_RATING_VALUE = 5;

    @Autowired
    public PassengersService(PassengersRepository passengersRepository, RatingsService ratingsService) {
        this.passengersRepository = passengersRepository;
        this.ratingsService = ratingsService;
    }

    public List<Passenger> findAll() throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findAll(Sort.by("lastname"));

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers");
        }

        return passengers.stream()
                .filter(passenger -> passenger.getStatus().equals("Active"))
                .collect(Collectors.toList());
    }

    public List<Passenger> findPagePassengers(int index, int count) throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository
                .findAll(PageRequest.of(index, count, Sort.by("lastname"))).getContent();

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers on this page");
        }

        return passengers.stream()
                .filter(passenger -> passenger.getStatus().equals("Active"))
                .collect(Collectors.toList());
    }

    public Passenger findById(long id) throws EntityNotFoundException {
        Optional<Passenger> passenger = passengersRepository.findById(id);

        return passenger.orElseThrow(EntityNotFoundException
                .entityNotFoundException("Passenger with id '" + id + "' wasn't found"));
    }

    public Passenger findByEmail(String email) throws EntityNotFoundException {
        Optional<Passenger> passenger = passengersRepository.findByEmail(email);

        return passenger.orElseThrow(EntityNotFoundException
                .entityNotFoundException("Passenger with email '" + email + "' wasn't found"));
    }

    public Passenger findByPhoneNumber(String phoneNumber) throws EntityNotFoundException {
        Optional<Passenger> passenger = passengersRepository.findByPhoneNumber(phoneNumber);

        return passenger.orElseThrow(EntityNotFoundException
                .entityNotFoundException("Passenger with phone number '" + phoneNumber + "' wasn't found"));
    }

    @Transactional
    public void save(Passenger passenger) {
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
    public void update(long id, Passenger passenger) throws EntityNotFoundException {
        Passenger passengerData = findById(id);
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
        Passenger passenger = findById(id);

        passenger.setStatus("Inactive");

        passengersRepository.save(passenger);
    }

    public PassengerRating getPassengerRating(long id) throws EntityNotFoundException {
        Passenger passenger = findById(id);

        return PassengerRating.builder()
                .value(ratingsService.calculatePassengerRating(passenger))
                .build();
    }

    @Transactional
    public void ratePassenger(long id, Rating rating) throws EntityNotFoundException {
        Passenger passenger = findById(id);
        rating.setPassenger(passenger);

        ratingsService.save(rating);
    }

    public PassengerProfile getPassengerProfile(long id) throws EntityNotFoundException {
        return PassengerProfile.builder()
                .passenger(findById(id))
                .rating(getPassengerRating(id))
                .build();
    }
}
