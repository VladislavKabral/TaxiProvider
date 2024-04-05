package by.modsen.taxiprovider.ridesservice.utility;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.AddressDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideListDto;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RidesTestUtil {

    public final long DEFAULT_RIDE_ID = 1;
    public final long DEFAULT_PASSENGER_ID = 1;
    public final long DEFAULT_DRIVER_ID = 1;
    public final LocalDateTime DEFAULT_STARTED_TIME = LocalDateTime.of(2024, 3, 29, 21, 50);
    public final LocalDateTime DEFAULT_ENDED_TIME = LocalDateTime.of(2024, 3, 29, 22, 30);
    public final AddressDto DEFAULT_SOURCE_ADDRESS = AddressDto.builder()
                .latitude(53.1234567)
                .longitude(90.1234567)
            .build();
    public final List<AddressDto> DEFAULT_DESTINATION_ADDRESSES = List.of(AddressDto.builder()
            .latitude(53.3456789)
            .longitude(90.3456789)
            .build());
    public final PromoCodeDto DEFAULT_PROMO_CODE = PromoCodeDto.builder()
            .id(1)
            .value("JAVA")
            .discount(0.5)
            .build();
    public final BigDecimal DEFAULT_COST = BigDecimal.valueOf(7.0);
    public final String DEFAULT_RIDE_STATUS = "COMPLETED";
    public final String DEFAULT_PAYMENT_TYPE = "CARD";

    public List<RideDto> getRides() {
        return List.of(RideDto.builder()
                        .id(DEFAULT_RIDE_ID)
                        .passengerId(DEFAULT_PASSENGER_ID)
                        .driverId(DEFAULT_DRIVER_ID)
                        .startedAt(DEFAULT_STARTED_TIME)
                        .endedAt(DEFAULT_ENDED_TIME)
                        .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                        .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                        .cost(DEFAULT_COST)
                        .promoCode(DEFAULT_PROMO_CODE)
                        .status(DEFAULT_RIDE_STATUS)
                        .paymentType(DEFAULT_PAYMENT_TYPE)
                .build());
    }

    public RideListDto getEmptyRideList() {
        return new RideListDto(new ArrayList<>());
    }

    public RideDto getRide() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .cost(DEFAULT_COST)
                .promoCode(DEFAULT_PROMO_CODE)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

    public NewRideDto getRequestForSaveRide() {
        return NewRideDto.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public NewRideDto getInvalidRequestForSaveRide() {
        return NewRideDto.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(null)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public RideDto getRequestForEditDrive() {
        return getRide();
    }

    public RideDto getInvalidRequestForEditDrive() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(null)
                .cost(DEFAULT_COST)
                .promoCode(DEFAULT_PROMO_CODE)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

    public RideResponseDto getResponse() {
        return new RideResponseDto(DEFAULT_RIDE_ID);
    }
}
