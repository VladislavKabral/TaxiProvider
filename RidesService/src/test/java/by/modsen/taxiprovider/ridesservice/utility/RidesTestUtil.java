package by.modsen.taxiprovider.ridesservice.utility;

import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.response.RideResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.AddressDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.NewRideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import by.modsen.taxiprovider.ridesservice.dto.ride.RideListDto;
import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RidesTestUtil {

    public final String GET_RIDES_PATH = "/rides";
    public final String GET_RIDE_PATH = "/rides/1";
    public final String GET_NON_EXISTS_RIDE_PATH = "/rides/6";
    public final String GET_PASSENGER_RIDES_PATH = "/rides/passenger/1";
    public final String GET_NON_EXISTS_PASSENGER_RIDES_PATH = "/rides/passenger/4";
    public final String GET_DRIVER_RIDES_PATH = "/rides/driver/1";
    public final String GET_NON_EXISTS_DRIVER_RIDES_PATH = "/rides/driver/4";
    public final String GET_FREE_DRIVERS_PATH = "/drivers/free";
    public final String GET_DRIVER_PATH = "/drivers/1";
    public final String PAYMENT_RIDE_PATH = "/payment/customerCharge";
    public final String DISTANCE_CALCULATION_PATH = "https://routing.api.2gis.com/get_dist_matrix?key=47c686cb-71fd-4917-86ae-e80624361317&version=2.0";
    public final long DEFAULT_RIDE_ID = 1;
    public final long DEFAULT_NON_EXISTS_RIDE_ID = 6;
    public final long DEFAULT_PASSENGER_ID = 1;
    public final long DEFAULT_NON_EXISTS_PASSENGER_ID = 4;
    public final long DEFAULT_DRIVER_ID = 1;
    public final String DEFAULT_DRIVER_LASTNAME = "Vasiliev";
    public final String DEFAULT_DRIVER_FIRSTNAME = "Platon";
    public final String DEFAULT_DRIVER_EMAIL = "mr.vasiliev@mail.ru";
    public final String DEFAULT_DRIVER_PHONE_NUMBER = "+375293660893";
    public final String DEFAULT_DRIVER_ACCOUNT_STATUS = "ACTIVE";
    public final String DEFAULT_DRIVER_STATUS = "FREE";
    public final BigDecimal DEFAULT_DRIVER_BALANCE = BigDecimal.valueOf(0.0);
    public final long DEFAULT_NON_EXISTS_DRIVER_ID = 4;
    public final LocalDateTime DEFAULT_STARTED_TIME = LocalDateTime.of(2024, 3, 29, 21, 50);
    public final LocalDateTime DEFAULT_ENDED_TIME = LocalDateTime.of(2024, 3, 29, 22, 30);
    public final AddressDto DEFAULT_SOURCE_ADDRESS_DTO = AddressDto.builder()
                .latitude(53.1234567)
                .longitude(90.1234567)
            .build();
    public final Address DEFAULT_SOURCE_ADDRESS = Address.builder()
            .latitude(String.valueOf(53.1234567))
            .longitude(String.valueOf(90.1234567))
            .build();
    public final List<AddressDto> DEFAULT_DESTINATION_ADDRESSES_DTO = List.of(AddressDto.builder()
            .latitude(53.3456789)
            .longitude(90.3456789)
            .build());
    public final List<Address> DEFAULT_DESTINATION_ADDRESSES = List.of(Address.builder()
            .latitude(String.valueOf(53.3456789))
            .longitude(String.valueOf(90.3456789))
            .build());
    public final PromoCodeDto DEFAULT_PROMO_CODE = PromoCodeDto.builder()
            .id(1)
            .value("JAVA")
            .discount(0.5)
            .build();
    public final BigDecimal DEFAULT_COST = BigDecimal.valueOf(7.0);
    public final String DEFAULT_RIDE_STATUS = "COMPLETED";
    public final String IN_PROGRESS_RIDE_STATUS = "IN_PROGRESS";
    public final String DEFAULT_PAYMENT_TYPE = "CARD";

    public List<RideDto> getRides() {
        return List.of(RideDto.builder()
                        .id(DEFAULT_RIDE_ID)
                        .passengerId(DEFAULT_PASSENGER_ID)
                        .driverId(DEFAULT_DRIVER_ID)
                        .startedAt(DEFAULT_STARTED_TIME)
                        .endedAt(DEFAULT_ENDED_TIME)
                        .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                        .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                        .cost(DEFAULT_COST)
                        .promoCode(DEFAULT_PROMO_CODE)
                        .status(DEFAULT_RIDE_STATUS)
                        .paymentType(DEFAULT_PAYMENT_TYPE)
                .build());
    }

    public List<RideDto> getEmptyRides() {
        return new ArrayList<>();
    }

    public List<Ride> getRidesList() {
        return List.of(Ride.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .cost(DEFAULT_COST)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build());
    }

    public List<Ride> getInProgressRides() {
        return List.of(Ride.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .cost(DEFAULT_COST)
                .status(IN_PROGRESS_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build());
    }

    public List<Ride> getDriversRides() {
        return List.of(Ride.builder()
                .id(DEFAULT_NON_EXISTS_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .cost(DEFAULT_COST)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build());
    }



    public RideListDto getEmptyRideList() {
        return new RideListDto(new ArrayList<>());
    }

    public RideListDto getRideListDto() {
        return new RideListDto(getRides());
    }

    public List<Ride> getEmptyRidesList() {
        return new ArrayList<>();
    }

    public List<RideDto> getEmptyRidesDtoList() {
        return new ArrayList<>();
    }

    public RideDto getRideDto() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                .cost(DEFAULT_COST)
                .promoCode(DEFAULT_PROMO_CODE)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

    public Ride getRide() {
        return Ride.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .cost(DEFAULT_COST)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

    public Ride getInvalidRide() {
        return Ride.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES)
                .cost(DEFAULT_COST)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType("KJHJKhnk")
                .build();
    }

    public NewRideDto getRequestForSaveRide() {
        return NewRideDto.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public NewRideDto getInvalidRequestForSaveRide() {
        return NewRideDto.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(null)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public NewRideDto getInvalidRequestWithInvalidPaymentTypeForSaveRide() {
        return NewRideDto.builder()
                .passengerId(DEFAULT_PASSENGER_ID)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                .paymentType("KJHJKhnk")
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public RideDto getInvalidRequestWithInvalidPaymentTypeForEditRide() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .cost(DEFAULT_COST)
                .promoCode(DEFAULT_PROMO_CODE)
                .status(IN_PROGRESS_RIDE_STATUS)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                .destinationAddresses(DEFAULT_DESTINATION_ADDRESSES_DTO)
                .paymentType("KJHJKhnk")
                .promoCode(DEFAULT_PROMO_CODE)
                .build();
    }

    public RideDto getRequestForEditDrive() {
        return getRideDto();
    }

    public RideDto getInvalidRequestForEditDrive() {
        return RideDto.builder()
                .id(DEFAULT_RIDE_ID)
                .passengerId(DEFAULT_PASSENGER_ID)
                .driverId(DEFAULT_DRIVER_ID)
                .startedAt(DEFAULT_STARTED_TIME)
                .endedAt(DEFAULT_ENDED_TIME)
                .sourceAddress(DEFAULT_SOURCE_ADDRESS_DTO)
                .destinationAddresses(null)
                .cost(DEFAULT_COST)
                .promoCode(DEFAULT_PROMO_CODE)
                .status(DEFAULT_RIDE_STATUS)
                .paymentType(DEFAULT_PAYMENT_TYPE)
                .build();
    }

    public RideResponseDto getResponse() {
        return new RideResponseDto(DEFAULT_RIDE_ID, DEFAULT_DRIVER_ID, DEFAULT_PASSENGER_ID);
    }

    public DriverListDto getFreeDrivers() {
        return DriverListDto.builder()
                .content(List.of(DriverDto.builder()
                                .id(DEFAULT_DRIVER_ID)
                                .lastname(DEFAULT_DRIVER_LASTNAME)
                                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                                .email(DEFAULT_DRIVER_EMAIL)
                                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                                .accountStatus(DEFAULT_DRIVER_ACCOUNT_STATUS)
                                .balance(DEFAULT_DRIVER_BALANCE)
                                .status(DEFAULT_DRIVER_STATUS)
                        .build()))
                .build();
    }

    public DriverDto getDriver() {
        return getFreeDrivers().getContent().get(0);
    }
}
