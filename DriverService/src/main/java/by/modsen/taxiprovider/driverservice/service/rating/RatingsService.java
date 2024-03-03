package by.modsen.taxiprovider.driverservice.service.rating;

import by.modsen.taxiprovider.driverservice.model.driver.Driver;
import by.modsen.taxiprovider.driverservice.model.rating.Rating;
import by.modsen.taxiprovider.driverservice.repository.rating.RatingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    private static final int GRADES_COUNT = 30;

    @Transactional
    public void save(Rating rating) {
        rating.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ratingsRepository.save(rating);
    }

    public double calculateDriverRating(Driver driver) {
        List<Rating> actualRatings = driver.getRatings()
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(GRADES_COUNT)
                .toList();

        return new BigDecimal(Double.toString((double) actualRatings.stream()
                .mapToInt(Rating::getValue)
                .sum()
                / actualRatings.size()))
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
