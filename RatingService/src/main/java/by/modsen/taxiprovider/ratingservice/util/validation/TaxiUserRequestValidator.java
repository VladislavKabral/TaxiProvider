package by.modsen.taxiprovider.ratingservice.util.validation;

import by.modsen.taxiprovider.ratingservice.dto.request.TaxiUserRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TaxiUserRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return TaxiUserRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TaxiUserRequestDto initRequestDTO = (TaxiUserRequestDto) target;

        if ((!initRequestDTO.getRole().equals("DRIVER")) && (!initRequestDTO.getRole().equals("PASSENGER"))) {
            errors.rejectValue("role", "", initRequestDTO.getRole() + " is wrong role name");
        }
    }
}
