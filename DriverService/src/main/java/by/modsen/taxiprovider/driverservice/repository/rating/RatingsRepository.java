package by.modsen.taxiprovider.driverservice.repository.rating;

import by.modsen.taxiprovider.driverservice.model.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsRepository extends JpaRepository<Rating, Long> {
}
