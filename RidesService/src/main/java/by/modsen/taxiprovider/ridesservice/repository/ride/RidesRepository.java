package by.modsen.taxiprovider.ridesservice.repository.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RidesRepository extends JpaRepository<Ride, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "ride_entity_graph")
    List<Ride> findByPassengerId(long passengerId);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "ride_entity_graph")
    List<Ride> findByDriverId(long driverId);
}
