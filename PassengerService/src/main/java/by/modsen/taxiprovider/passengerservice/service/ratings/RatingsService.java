package by.modsen.taxiprovider.passengerservice.service.ratings;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import by.modsen.taxiprovider.passengerservice.model.rating.Rating;
import by.modsen.taxiprovider.passengerservice.repository.rating.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
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
        DecimalFormat df = new DecimalFormat("#.#");

        List<Rating> actualRatings = passenger.getRatings()
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(30)
                .toList();

        for (Rating rating: actualRatings) {
            System.out.println(rating.getValue());
        }

        return Double.parseDouble(df.format((double) actualRatings.stream()
                .mapToInt(Rating::getValue).sum()
                / actualRatings.size()));
    }

}
