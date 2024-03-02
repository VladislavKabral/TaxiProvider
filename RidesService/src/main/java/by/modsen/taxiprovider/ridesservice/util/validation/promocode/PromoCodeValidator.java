package by.modsen.taxiprovider.ridesservice.util.validation.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class PromoCodeValidator implements Validator {

    private final PromoCodesService promoCodesService;

    private final PromoCodeMapper promoCodeMapper;

    private static final double MINIMAL_DISCOUNT = 0.01;

    private static final double MAXIMUM_DISCOUNT = 1.0;

    @Override
    public boolean supports(Class<?> clazz) {
        return PromoCodeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PromoCode promoCode = promoCodeMapper.toEntity((PromoCodeDTO) target);

        try {
            PromoCode existingPromoCode = promoCodeMapper.toEntity(promoCodesService.findByValue(promoCode.getValue()));
            if ((existingPromoCode != null) && (existingPromoCode.getId() != promoCode.getId())) {
                errors.rejectValue("value", "", "Promo code '" + promoCode.getValue() +
                        "' already exists");
            }
        } catch (EntityNotFoundException e) {
            //TODO: add log
        }

        if ((Double.compare(promoCode.getDiscount(), MINIMAL_DISCOUNT) == -1)
                || (Double.compare(promoCode.getDiscount(), MAXIMUM_DISCOUNT) == 1)) {
            errors.rejectValue("discount", "",
        "Invalid discount's value. Discount must be equal or less than 1.0 and equal or more than 0.01");
        }
    }
}
