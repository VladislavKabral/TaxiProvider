package by.modsen.taxiprovider.ridesservice.contract;

import by.modsen.taxiprovider.ridesservice.RidesServiceApplication;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.RidesService;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.context.WebApplicationContext;

import static by.modsen.taxiprovider.ridesservice.utility.RidesTestUtil.*;
import static by.modsen.taxiprovider.ridesservice.utility.PromoCodesTestUtil.*;
import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = RidesServiceApplication.class)
public class BaseTestClass {

    @MockBean
    private RidesService ridesService;

    @MockBean
    private PromoCodesService promoCodesService;

    @Autowired
    protected WebApplicationContext context;

    @SneakyThrows
    @BeforeEach
    public void setUp() {
        when(ridesService.findByDriverId(DEFAULT_DRIVER_ID)).thenReturn(getRideListDto());

        when(ridesService.findByPassengerId(DEFAULT_PASSENGER_ID)).thenReturn(getRideListDto());

        when(promoCodesService.findAll()).thenReturn(getPromoCodesListDto());

        when(ridesService.findAll()).thenReturn(getRideListDto());

        when(ridesService.cancel(anyLong())).thenReturn(new RideResponseDto(DEFAULT_RIDE_ID));

        when(promoCodesService.save(any())).thenReturn(new PromoCodeResponseDto(NON_EXISTS_PROMO_CODE_ID));

        when(ridesService.save(any())).thenReturn(new RideResponseDto(DEFAULT_NON_EXISTS_RIDE_ID));

        when(promoCodesService.update(anyLong(), any())).thenReturn(new PromoCodeResponseDto(DEFAULT_PROMO_CODE_ID));

        when(ridesService.findById(DEFAULT_RIDE_ID)).thenReturn(getRideDto());

        when(ridesService.findById(DEFAULT_NON_EXISTS_RIDE_ID))
                .thenThrow(new EntityNotFoundException(String.format(RIDE_NOT_FOUND, DEFAULT_NON_EXISTS_RIDE_ID)));

        when(promoCodesService.findByValue(DEFAULT_PROMO_CODE_VALUE)).thenReturn(getPromoCodeDto());

        when(promoCodesService.findByValue(NON_EXISTS_PROMO_CODE_VALUE))
                .thenThrow(new EntityNotFoundException(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, NON_EXISTS_PROMO_CODE_VALUE)));

        RestAssuredMockMvc.webAppContextSetup(this.context);
    }
}
