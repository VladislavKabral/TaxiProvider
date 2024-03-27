package by.modsen.taxiprovider.ratingservice.repository;

import by.modsen.taxiprovider.ratingservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByTaxiUserIdAndRole(long taxiUserId, String role);
}
