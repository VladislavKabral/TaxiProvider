package by.modsen.taxiprovider.paymentservice.util.validation;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.Year;
import java.util.Calendar;

@Component
public class CardRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CardRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CardRequestDto cardRequestDTO = (CardRequestDto) target;

        int currentYear = Year.now().getValue();
        int currentMonth = Calendar.MONTH + 1;
        if ((currentYear == cardRequestDTO.getYear()) && (currentMonth >= cardRequestDTO.getMonth())) {
            errors.rejectValue("month", "",
                    "Bank card's expiration is invalid");
        }

        if (currentYear > cardRequestDTO.getYear()) {
            errors.rejectValue("year", "",
                    "Bank card's expiration is invalid");
        }
    }
}
