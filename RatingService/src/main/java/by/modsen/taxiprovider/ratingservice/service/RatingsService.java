package by.modsen.taxiprovider.ratingservice.service;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDTO;
import by.modsen.taxiprovider.ratingservice.mapper.RatingMapper;
import by.modsen.taxiprovider.ratingservice.model.Rating;
import by.modsen.taxiprovider.ratingservice.repository.RatingsRepository;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
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

    private final RatingMapper ratingMapper;

    private static final int GRADES_COUNT = 30;

    public TaxiUserRatingDTO getTaxiUserRating(long taxiUserId, String role) throws EntityNotFoundException {
        List<Rating> ratings = ratingsRepository.findByTaxiUserIdAndRole(taxiUserId, role)
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(GRADES_COUNT)
                .toList();

        return TaxiUserRatingDTO.builder()
                .taxiUserId(taxiUserId)
                .role(role)
                .value(new BigDecimal(Double.toString((double) ratings.stream()
                        .mapToInt(Rating::getValue)
                        .sum()
                        / ratings.size()))
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue())
                .build();
    }

    @Transactional
    public void save(RatingDTO ratingDTO) {
        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ratingsRepository.save(rating);
    }
}
