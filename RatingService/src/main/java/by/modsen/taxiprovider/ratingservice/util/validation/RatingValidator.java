package by.modsen.taxiprovider.ratingservice.util.validation;

import by.modsen.taxiprovider.ratingservice.dto.rating.RatingDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RatingValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return RatingDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RatingDTO ratingDTO = (RatingDTO) target;

        if ((!ratingDTO.getRole().equals("DRIVER")) && (!ratingDTO.getRole().equals("PASSENGER"))) {
            errors.rejectValue("role", "", ratingDTO.getRole() + " is wrong role name");
        }
    }
}
