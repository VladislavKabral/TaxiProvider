package by.modsen.taxiprovider.ridesservice.util.validation.promocode;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromoCodeValidator {

    private final PromoCodesRepository promoCodesRepository;

    private static final double MINIMAL_DISCOUNT = 0.01;

    private static final double MAXIMUM_DISCOUNT = 1.0;

    public void validate(PromoCode promoCode) throws EntityValidateException {
        Optional<PromoCode> existingPromoCode = promoCodesRepository.findByValue(promoCode.getValue());
        if ((existingPromoCode.isPresent()) && (existingPromoCode.get().getId() != promoCode.getId())) {
            log.info(String.format(PROMO_CODE_ALREADY_EXISTS, promoCode.getValue()));
            throw new EntityValidateException(String.format(PROMO_CODE_ALREADY_EXISTS, promoCode.getValue()));
        }

        if ((Double.compare(promoCode.getDiscount(), MINIMAL_DISCOUNT) == -1)
                || (Double.compare(promoCode.getDiscount(), MAXIMUM_DISCOUNT) == 1)) {
            log.info(DISCOUNT_IS_INVALID);
            throw new EntityValidateException(DISCOUNT_IS_INVALID);
        }
    }
}
