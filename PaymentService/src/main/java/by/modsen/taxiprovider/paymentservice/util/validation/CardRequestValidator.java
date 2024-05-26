package by.modsen.taxiprovider.paymentservice.util.validation;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDto;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

import java.time.Year;
import java.util.Calendar;

@Component
@Slf4j
public class CardRequestValidator {

    public void validate(CardRequestDto cardRequestDTO) throws EntityValidateException {
        int currentYear = Year.now().getValue();
        int currentMonth = Calendar.MONTH + 1;
        if ((currentYear == cardRequestDTO.getYear()) && (currentMonth >= cardRequestDTO.getMonth())) {
            log.info(BANK_CARD_EXPIRATION_IS_INVALID);
            throw new EntityValidateException(BANK_CARD_EXPIRATION_IS_INVALID);
        }

        if (currentYear > cardRequestDTO.getYear()) {
            log.info(BANK_CARD_EXPIRATION_IS_INVALID);
            throw new EntityValidateException(BANK_CARD_EXPIRATION_IS_INVALID);
        }

    }
}
