package by.modsen.taxiprovider.driverservice.repository;

import by.modsen.taxiprovider.driverservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriversRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByAccountStatus(String accountStatus);

    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByPhoneNumber(String phoneNumber);

    List<Driver> findByStatus(String status);
}
