package by.modsen.taxiprovider.ratingservice.util.validation;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.ratingservice.util.Message.*;

@Component
public class RatingValidator {

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    public void validate(RatingDto ratingDTO) throws EntityValidateException {
        if ((!ratingDTO.getRole().equals(DRIVER_ROLE_NAME)) && (!ratingDTO.getRole().equals(PASSENGER_ROLE_NAME))) {
            throw new EntityValidateException(String.format(ROLE_IS_INVALID, ratingDTO.getRole()));
        }
    }
}
