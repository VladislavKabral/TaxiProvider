package by.modsen.taxiprovider.passengerservice.repository.passenger;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengersRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhoneNumber(String phoneNumber);
}
