package by.modsen.taxiprovider.ratingservice.service;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDto;
import by.modsen.taxiprovider.ratingservice.mapper.RatingMapper;
import by.modsen.taxiprovider.ratingservice.model.Rating;
import by.modsen.taxiprovider.ratingservice.repository.RatingsRepository;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ratingservice.util.validation.RatingValidator;
import by.modsen.taxiprovider.ratingservice.util.validation.TaxiUserRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static by.modsen.taxiprovider.ratingservice.util.Message.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    private final RatingMapper ratingMapper;

    private final TaxiUserRequestValidator taxiUserRequestValidator;

    private final RatingValidator ratingValidator;

    private static final int GRADES_COUNT = 30;

    private static final int INIT_GRADE = 5;

    public TaxiUserRatingDto getTaxiUserRating(TaxiUserRequestDto request)
            throws EntityNotFoundException {
        log.info(String.format(GETTING_RATING, request.getTaxiUserId(), request.getRole()));
        List<Rating> ratings = ratingsRepository.findByTaxiUserIdAndRole(request.getTaxiUserId(), request.getRole())
                .stream()
                .sorted(Collections.reverseOrder())
                .limit(GRADES_COUNT)
                .toList();

        if (ratings.isEmpty()) {
            throw new EntityNotFoundException(String.format(TAXI_USER_NOT_FOUND,
                    request.getTaxiUserId(),
                    request.getRole()));
        }

        double ratingValue = new BigDecimal(Double.toString((double) ratings.stream()
                .mapToInt(Rating::getValue)
                .sum()
                / ratings.size()))
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();

        log.info(String.format(RATING_WAS_FOUND, ratingValue));
        return TaxiUserRatingDto.builder()
                .taxiUserId(request.getTaxiUserId())
                .role(request.getRole())
                .value(ratingValue)
                .build();
    }

    @Transactional
    public RatingResponseDto initTaxiUserRatings(TaxiUserRequestDto request) throws EntityValidateException {
        log.info(String.format(INITIALIZATION_TAXI_USER_RATING, request.getTaxiUserId(), request.getRole()));
        taxiUserRequestValidator.validate(request);

        for (int i = 0; i < GRADES_COUNT; i++) {
            Rating rating = Rating.builder()
                    .taxiUserId(request.getTaxiUserId())
                    .role(request.getRole())
                    .value(INIT_GRADE)
                    .createdAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                    .build();
            ratingsRepository.save(rating);
        }

        log.info(RATING_WAS_INITIALIZED);
        return RatingResponseDto.builder()
                .taxiUserId(request.getTaxiUserId())
                .role(request.getRole())
                .value(INIT_GRADE)
                .build();
    }

    @Transactional
    public RatingResponseDto save(RatingDto ratingDTO) throws EntityValidateException {
        log.info(String.format(RATING_TAXI_USER,
                ratingDTO.getTaxiUserId(),
                ratingDTO.getRole(),
                ratingDTO.getValue()));
        ratingValidator.validate(ratingDTO);

        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ratingsRepository.save(rating);

        log.info(TAXI_USER_WAS_RATED);
        return RatingResponseDto.builder()
                .taxiUserId(rating.getTaxiUserId())
                .role(rating.getRole())
                .value(rating.getValue())
                .build();
    }
}
