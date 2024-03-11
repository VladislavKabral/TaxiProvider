package by.modsen.taxiprovider.driverservice.repository.driver;

import by.modsen.taxiprovider.driverservice.model.driver.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriversRepository extends JpaRepository<Driver, Long> {

    List<Driver> findByStatus(String status);

    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByPhoneNumber(String phoneNumber);

    List<Driver> findByRideStatus(String rideStatus);
}
