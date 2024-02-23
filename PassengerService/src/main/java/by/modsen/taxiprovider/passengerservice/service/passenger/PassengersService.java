package by.modsen.taxiprovider.passengerservice.service.passenger;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.passenger.PassengerProfile;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.repository.passenger.PassengersRepository;
import by.modsen.taxiprovider.passengerservice.service.ratings.RatingsService;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PassengersService {
    private final PassengersRepository passengersRepository;

    private final RatingsService ratingsService;

    @Autowired
    public PassengersService(PassengersRepository passengersRepository, RatingsService ratingsService) {
        this.passengersRepository = passengersRepository;
        this.ratingsService = ratingsService;
    }

    public List<Passenger> findAll() throws EntityNotFoundException {
        List<Passenger> passengers = passengersRepository.findAll();

        if (passengers.isEmpty()) {
            throw new EntityNotFoundException("There aren't any passengers");
        }

        return passengers;
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

        for (int i = 0; i < 30; i++) {
            Rating rating = new Rating();
            rating.setValue(5);
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

    public double getPassengerRating(long id) throws EntityNotFoundException {
        Passenger passenger = findById(id);

        return ratingsService.calculatePassengerRating(passenger);
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
