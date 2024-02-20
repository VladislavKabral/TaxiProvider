package by.modsen.taxiprovider.passengerservice.service;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.repository.passenger.PassengersRepository;
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

    @Autowired
    public PassengersService(PassengersRepository passengersRepository) {
        this.passengersRepository = passengersRepository;
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

    @Transactional
    public void save(Passenger passenger) {
        passengersRepository.save(passenger);
    }

    @Transactional
    public void update(long id, Passenger passenger) {
        passenger.setId(id);
        passengersRepository.save(passenger);
    }

    @Transactional
    public void delete(long id) {
        passengersRepository.deleteById(id);
    }
}
