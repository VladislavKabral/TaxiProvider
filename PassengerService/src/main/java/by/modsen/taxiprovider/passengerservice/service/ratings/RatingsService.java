package by.modsen.taxiprovider.passengerservice.service.ratings;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.repository.rating.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    @Autowired
    public RatingsService(RatingsRepository ratingsRepository) {
        this.ratingsRepository = ratingsRepository;
    }

    @Transactional
    public void save(Rating rating) {
        rating.setCreatedAt(LocalDate.now());
        ratingsRepository.save(rating);
    }

    public double calculatePassengerRating(Passenger passenger) {
        List<Rating> actualRatings = passenger.getRatings()
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(30)
                .toList();

        return new BigDecimal(Double.toString((double) actualRatings.stream()
                .mapToInt(Rating::getValue)
                .sum()
                / actualRatings.size()))
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
