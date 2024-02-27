package by.modsen.taxiprovider.ridesservice.repository.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RidesRepository extends JpaRepository<Ride, Long> {

    List<Ride> findByPassengerId(long passengerId);

    List<Ride> findByDriverId(long driverId);
}
