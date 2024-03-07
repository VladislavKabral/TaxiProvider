package by.modsen.taxiprovider.ridesservice.util.validation.promocode;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class PromoCodeValidator implements Validator {

    private final PromoCodesRepository promoCodesRepository;

    private static final double MINIMAL_DISCOUNT = 0.01;

    private static final double MAXIMUM_DISCOUNT = 1.0;

    @Override
    public boolean supports(Class<?> clazz) {
        return PromoCode.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PromoCode promoCode = (PromoCode) target;

        Optional<PromoCode> existingPromoCode = promoCodesRepository.findByValue(promoCode.getValue());
        if ((existingPromoCode.isPresent()) && (existingPromoCode.get().getId() != promoCode.getId())) {
            errors.rejectValue("value", "", "Promo code '" + promoCode.getValue() +
                    "' already exists");
        }

        if ((Double.compare(promoCode.getDiscount(), MINIMAL_DISCOUNT) == -1)
                || (Double.compare(promoCode.getDiscount(), MAXIMUM_DISCOUNT) == 1)) {
            errors.rejectValue("discount", "",
        "Invalid discount's value. Discount must be equal or less than 1.0 and equal or more than 0.01");
        }
    }
}
