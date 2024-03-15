package by.modsen.taxiprovider.passengerservice.repository;

import by.modsen.taxiprovider.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassengersRepository extends JpaRepository<Passenger, Long> {

    Optional<Passenger> findByEmail(String email);

    Optional<Passenger> findByPhoneNumber(String phoneNumber);

    List<Passenger> findByStatusOrderByLastname(String status);
}
