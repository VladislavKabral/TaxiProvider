package by.modsen.taxiprovider.ridesservice.utility;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class PromoCodesTestUtil {

    public final long DEFAULT_PROMO_CODE_ID = 1;
    public final String DEFAULT_PROMO_CODE_VALUE = "JAVA";
    public final double DEFAULT_PROMO_CODE_DISCOUNT = 0.25;

    public List<PromoCodeDTO> getPromoCodes() {
        return List.of(PromoCodeDTO.builder()
                        .id(DEFAULT_PROMO_CODE_ID)
                        .value(DEFAULT_PROMO_CODE_VALUE)
                        .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build(),
                PromoCodeDTO.builder()
                        .id(2)
                        .value("MODSEN")
                        .discount(0.4)
                .build(),
                PromoCodeDTO.builder()
                        .id(3)
                        .value("KABRAL")
                        .discount(0.5)
                        .build());
    }

    public PromoCodeDTO getPromoCode() {
        return PromoCodeDTO.builder()
                .id(DEFAULT_PROMO_CODE_ID)
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDTO getRequestForSavePromoCode() {
        return PromoCodeDTO.builder()
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDTO getInvalidRequestForSavePromoCode() {
        return PromoCodeDTO.builder()
                .value("J")
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDTO getRequestForEditPromoCode() {
        return PromoCodeDTO.builder()
                .id(DEFAULT_PROMO_CODE_ID)
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDTO getInvalidRequestForEditPromoCode() {
        return PromoCodeDTO.builder()
                .id(DEFAULT_PROMO_CODE_ID)
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(4.0)
                .build();
    }

    public PromoCodeResponseDTO getResponse() {
        return new PromoCodeResponseDTO(DEFAULT_PROMO_CODE_ID);
    }
}
