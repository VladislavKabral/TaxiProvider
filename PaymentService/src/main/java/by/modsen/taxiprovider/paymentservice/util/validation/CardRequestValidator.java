package by.modsen.taxiprovider.paymentservice.util.validation;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

import java.time.Year;
import java.util.Calendar;

@Component
public class CardRequestValidator implements Validator {

    private static final String MONTH_FIELD_NAME = "month";

    private static final String YEAR_FIELD_NAME = "year";

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
            errors.rejectValue(MONTH_FIELD_NAME, "", BANK_CARD_EXPIRATION_IS_INVALID);
        }

        if (currentYear > cardRequestDTO.getYear()) {
            errors.rejectValue(YEAR_FIELD_NAME, "", BANK_CARD_EXPIRATION_IS_INVALID);
        }
    }
}
