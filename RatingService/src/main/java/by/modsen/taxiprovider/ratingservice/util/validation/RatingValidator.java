package by.modsen.taxiprovider.ratingservice.util.validation;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.ratingservice.util.Message.*;

@Component
@Slf4j
public class RatingValidator {

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    public void validate(RatingDto ratingDTO) throws EntityValidateException {
        if ((!ratingDTO.getRole().equals(DRIVER_ROLE_NAME)) && (!ratingDTO.getRole().equals(PASSENGER_ROLE_NAME))) {
            String message = String.format(ROLE_IS_INVALID, ratingDTO.getRole());
            log.info(message);
            throw new EntityValidateException(message);
        }
    }
}
