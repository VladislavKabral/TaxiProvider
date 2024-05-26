package by.modsen.taxiprovider.ratingservice.repository;

import by.modsen.taxiprovider.ratingservice.model.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends MongoRepository<Rating, Integer> {

    List<Rating> findByTaxiUserIdAndRole(long taxiUserId, String role);
}
