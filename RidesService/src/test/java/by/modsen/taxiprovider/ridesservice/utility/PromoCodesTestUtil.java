package by.modsen.taxiprovider.ridesservice.utility;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodesListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PromoCodesTestUtil {

    public final long DEFAULT_PROMO_CODE_ID = 1;
    public final String DEFAULT_PROMO_CODE_VALUE = "JAVA";
    public final double DEFAULT_PROMO_CODE_DISCOUNT = 0.25;

    public List<PromoCodeDto> getPromoCodes() {
        return List.of(PromoCodeDto.builder()
                        .id(DEFAULT_PROMO_CODE_ID)
                        .value(DEFAULT_PROMO_CODE_VALUE)
                        .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build(),
                PromoCodeDto.builder()
                        .id(2)
                        .value("MODSEN")
                        .discount(0.4)
                .build(),
                PromoCodeDto.builder()
                        .id(3)
                        .value("KABRAL")
                        .discount(0.5)
                        .build());
    }

    public PromoCodesListDto getEmptyPromoCodesList() {
        return new PromoCodesListDto(new ArrayList<>());
    }

    public PromoCodeDto getPromoCode() {
        return PromoCodeDto.builder()
                .id(DEFAULT_PROMO_CODE_ID)
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDto getRequestForSavePromoCode() {
        return PromoCodeDto.builder()
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDto getInvalidRequestForSavePromoCode() {
        return PromoCodeDto.builder()
                .value("J")
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDto getRequestForEditPromoCode() {
        return PromoCodeDto.builder()
                .id(DEFAULT_PROMO_CODE_ID)
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(DEFAULT_PROMO_CODE_DISCOUNT)
                .build();
    }

    public PromoCodeDto getInvalidRequestForEditPromoCode() {
        return PromoCodeDto.builder()
                .id(DEFAULT_PROMO_CODE_ID)
                .value(DEFAULT_PROMO_CODE_VALUE)
                .discount(4.0)
                .build();
    }

    public PromoCodeResponseDto getResponse() {
        return new PromoCodeResponseDto(DEFAULT_PROMO_CODE_ID);
    }
}
