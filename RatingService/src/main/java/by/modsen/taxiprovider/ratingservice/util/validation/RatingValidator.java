package by.modsen.taxiprovider.ratingservice.util.validation;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static by.modsen.taxiprovider.ratingservice.util.Message.*;

@Component
public class RatingValidator implements Validator {

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String ROLE_FIELD_NAME = "role";

    @Override
    public boolean supports(Class<?> clazz) {
        return RatingDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RatingDto ratingDTO = (RatingDto) target;

        if ((!ratingDTO.getRole().equals(DRIVER_ROLE_NAME)) && (!ratingDTO.getRole().equals(PASSENGER_ROLE_NAME))) {
            errors.rejectValue(ROLE_FIELD_NAME, "", String.format(ROLE_IS_INVALID, ratingDTO.getRole()));
        }
    }
}
