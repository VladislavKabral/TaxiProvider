package by.modsen.taxiprovider.endtoendtest.util;


import by.modsen.taxiprovider.endtoendtest.dto.request.NewRideRequestDto;
import by.modsen.taxiprovider.endtoendtest.dto.ride.AddressDto;
import by.modsen.taxiprovider.endtoendtest.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.endtoendtest.dto.ride.RideDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class EndToEntTestUtil {

    public static final String DRIVERS_SERVICE_PATH = "http://localhost:8081/drivers";
    public static final String PASSENGERS_SERVICE_PATH = "http://localhost:8080/passengers";
    public static final String RIDES_SERVICE_PATH = "http://localhost:8082/rides";
    public static final String PAYMENT_SERVICE_PATH = "http://localhost:8083/payment";
    public final String GET_PASSENGER_RIDE_PATH = "/{passengerId}";
    public final String GET_PASSENGER_RIDES_PATH = "/passenger/{id}";
    public final String GET_DRIVER_RIDES_PATH = "/driver/{id}";
    public final String GET_FREE_DRIVERS_PATH = "/free";
    public final String GET_DRIVER_PATH = "/{id}";
    public final String PAYMENT_RIDE_PATH = "/customerCharge";
    public final String GET_PROFILE_PATH = "/{id}/profile";
    public final String CONTENT_TYPE = "application/json";

    public final long DEFAULT_RIDE_ID = 1;
    public final long DEFAULT_PASSENGER_ID = 1;
    public final long DEFAULT_DRIVER_ID = 1;
    public final String DEFAULT_DRIVER_STATUS = "FREE";
    public final AddressDto DEFAULT_SOURCE_ADDRESS_DTO = AddressDto.builder()
            .latitude(53.1234567)
            .longitude(90.1234567)
            .build();
    public final List<AddressDto> DEFAULT_DESTINATION_ADDRESSES_DTO = List.of(AddressDto.builder()
            .latitude(53.3456789)
            .longitude(90.3456789)
            .build());
    public final PromoCodeDto DEFAULT_PROMO_CODE = PromoCodeDto.builder()
            .id(1)
            .value("JAVA")
            .discount(0.5)
            .build();
    public final String DEFAULT_RIDE_STATUS = "COMPLETED";
    public final String IN_PROGRESS_RIDE_STATUS = "IN_PROGRESS";
    public final String DEFAULT_PAYMENT_TYPE = "CARD";
    public final String DRIVER_STATUS_TAKEN = "TAKEN";

    public NewRideRequestDto getRequestForSaveRide() {
        return NewRideRequestDto.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public RideDto getRequestForStartRide() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .status(IN_PROGRESS_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

    public RideDto getRequestForEndRide() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

}
