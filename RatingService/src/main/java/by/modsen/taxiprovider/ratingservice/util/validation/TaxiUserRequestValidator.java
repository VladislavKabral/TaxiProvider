package by.modsen.taxiprovider.ratingservice.util.validation;

import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static by.modsen.taxiprovider.ratingservice.util.Message.*;

@Component
public class TaxiUserRequestValidator implements Validator {

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String ROLE_FIELD_NAME = "role";

    @Override
    public boolean supports(Class<?> clazz) {
        return TaxiUserRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TaxiUserRequestDto initRequestDTO = (TaxiUserRequestDto) target;

        if ((!initRequestDTO.getRole().equals(DRIVER_ROLE_NAME))
                && (!initRequestDTO.getRole().equals(PASSENGER_ROLE_NAME))) {
            errors.rejectValue(ROLE_FIELD_NAME, "", String.format(ROLE_IS_INVALID, initRequestDTO.getRole()));
        }
    }
}
