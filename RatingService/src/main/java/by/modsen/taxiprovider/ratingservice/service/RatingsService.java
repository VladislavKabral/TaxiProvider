package by.modsen.taxiprovider.ratingservice.service;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.rating.TaxiUserRatingDTO;
import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDTO;
import by.modsen.taxiprovider.ratingservice.dto.response.RatingResponseDTO;
import by.modsen.taxiprovider.ratingservice.mapper.RatingMapper;
import by.modsen.taxiprovider.ratingservice.model.Rating;
import by.modsen.taxiprovider.ratingservice.repository.RatingsRepository;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ratingservice.util.validation.RatingValidator;
import by.modsen.taxiprovider.ratingservice.util.validation.TaxiUserRequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static by.modsen.taxiprovider.ratingservice.util.Message.TAXI_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    private final RatingMapper ratingMapper;

    private final TaxiUserRequestValidator taxiUserRequestValidator;

    private final RatingValidator ratingValidator;

    private static final int GRADES_COUNT = 30;

    private static final int INIT_GRADE = 5;

    public TaxiUserRatingDTO getTaxiUserRating(TaxiUserRequestDTO request)
            throws EntityNotFoundException {
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

        return TaxiUserRatingDTO.builder()
                .taxiUserId(request.getTaxiUserId())
                .role(request.getRole())
                .value(new BigDecimal(Double.toString((double) ratings.stream()
                        .mapToInt(Rating::getValue)
                        .sum()
                        / ratings.size()))
                        .setScale(1, RoundingMode.HALF_UP)
                        .doubleValue())
                .build();
    }

    @Transactional
    public RatingResponseDTO initTaxiUserRatings(TaxiUserRequestDTO request, BindingResult bindingResult)
            throws EntityValidateException {
        taxiUserRequestValidator.validate(request, bindingResult);
        handleBindingResult(bindingResult);

        for (int i = 0; i < GRADES_COUNT; i++) {
            Rating rating = Rating.builder()
                    .taxiUserId(request.getTaxiUserId())
                    .role(request.getRole())
                    .value(INIT_GRADE)
                    .createdAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                    .build();
            ratingsRepository.save(rating);
        }

        return RatingResponseDTO.builder()
                .taxiUserid(request.getTaxiUserId())
                .role(request.getRole())
                .value(INIT_GRADE)
                .build();
    }

    @Transactional
    public RatingResponseDTO save(RatingDTO ratingDTO, BindingResult bindingResult) throws EntityValidateException {
        ratingValidator.validate(ratingDTO, bindingResult);
        handleBindingResult(bindingResult);

        Rating rating = ratingMapper.toEntity(ratingDTO);
        rating.setCreatedAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime());
        ratingsRepository.save(rating);

        return RatingResponseDTO.builder()
                .taxiUserid(rating.getTaxiUserId())
                .role(rating.getRole())
                .value(rating.getValue())
                .build();
    }

    private void handleBindingResult(BindingResult bindingResult) throws EntityValidateException {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();

            for (FieldError error: bindingResult.getFieldErrors()) {
                message.append(error.getDefaultMessage()).append(". ");
            }

            throw new EntityValidateException(message.toString());
        }
    }
}
