package by.modsen.taxiprovider.passengerservice.service.ratings;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.repository.rating.RatingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    private static final int COUNT_OF_GRADES = 30;

    @Transactional
    public void save(Rating rating) {
        rating.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ratingsRepository.save(rating);
    }

    public double calculatePassengerRating(Passenger passenger) {
        List<Rating> actualRatings = passenger.getRatings()
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(COUNT_OF_GRADES)
                .toList();

        return new BigDecimal(Double.toString((double) actualRatings.stream()
                .mapToInt(Rating::getValue)
                .sum()
                / actualRatings.size()))
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
