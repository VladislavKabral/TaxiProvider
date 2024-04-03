package by.modsen.taxiprovider.ridesservice.utility;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.AddressDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class RidesTestUtil {

    public final long DEFAULT_RIDE_ID = 1;
    public final long DEFAULT_PASSENGER_ID = 1;
    public final long DEFAULT_DRIVER_ID = 1;
    public final LocalDateTime DEFAULT_STARTED_TIME = LocalDateTime.of(2024, 3, 29, 21, 50);
    public final LocalDateTime DEFAULT_ENDED_TIME = LocalDateTime.of(2024, 3, 29, 22, 30);
    public final AddressDTO DEFAULT_SOURCE_ADDRESS = AddressDTO.builder()
                .latitude(53.1234567)
                .longitude(90.1234567)
            .build();
    public final List<AddressDTO> DEFAULT_DESTINATION_ADDRESSES = List.of(AddressDTO.builder()
            .longitude(53.3456789)
            .latitude(90.3456789)
            .build());
    public final PromoCodeDTO DEFAULT_PROMO_CODE = PromoCodeDTO.builder()
            .id(1)
            .value("JAVA")
            .discount(0.5)
            .build();
    public final BigDecimal DEFAULT_COST = BigDecimal.valueOf(7.0);
    public final String DEFAULT_RIDE_STATUS = "COMPLETED";
    public final String DEFAULT_PAYMENT_TYPE = "CARD";

    public List<RideDTO> getRides() {
        return List.of(RideDTO.builder()
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

    public RideDTO getRide() {
        return RideDTO.builder()
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

    public NewRideDTO getRequestForSaveRide() {
        return NewRideDTO.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public NewRideDTO getInvalidRequestForSaveRide() {
        return NewRideDTO.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(null)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public RideDTO getRequestForEditDrive() {
        return getRide();
    }

    public RideDTO getInvalidRequestForEditDrive() {
        return RideDTO.builder()
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

    public RideResponseDTO getResponse() {
        return new RideResponseDTO(DEFAULT_RIDE_ID);
    }
}
