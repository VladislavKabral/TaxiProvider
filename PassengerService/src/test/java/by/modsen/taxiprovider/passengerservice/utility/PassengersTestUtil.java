package by.modsen.taxiprovider.passengerservice.utility;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerListDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PassengersTestUtil {

    public final long DEFAULT_PASSENGER_ID = 1;
    public final String DEFAULT_SORT_FIELD = "lastname";
    public final int DEFAULT_PAGE = 1;
    public final int DEFAULT_PAGE_SIZE = 3;
    public final int DEFAULT_INVALID_PAGE = -1;
    public final int DEFAULT_INVALID_PAGE_SIZE = 0;
    public final double DEFAULT_PASSENGER_RATING = 5.0;
    public final String DEFAULT_PASSENGER_LASTNAME = "Ivanov";
    public final String DEFAULT_PASSENGER_FIRSTNAME = "Ivan";
    public final String DEFAULT_PASSENGER_EMAIL = "mr.ivanov@mail.ru";
    public final String DEFAULT_PASSENGER_PHONE_NUMBER = "+375291234590";
    public final String DEFAULT_PASSENGER_PASSWORD = "$2a$12$kjMvQl359in5BXoX1mgAhORCAwfCU3OREAAR1wX9444WVoMvORKiW";

    public List<PassengerDto> getPassengers() {
        return List.of(PassengerDto.builder()
                        .id(1)
                        .lastname("Ivanov")
                        .firstname("Ivan")
                        .email("mr.ivanov@mail.ru")
                        .phoneNumber("+375291234590")
                        .build(),
                PassengerDto.builder()
                        .id(2)
                        .lastname("Petrov")
                        .firstname("Petr")
                        .email("mr.petrov@mail.ru")
                        .phoneNumber("+375336894867")
                        .build(),
                PassengerDto.builder()
                        .id(3)
                        .lastname("Borisov")
                        .firstname("Boris")
                        .email("mr.borisov@mail.ru")
                        .phoneNumber("+375296131124")
                        .build());
    }

    public PassengerListDto getEmptyPassengersList() {
        return new PassengerListDto(new ArrayList<>());
    }

    public PassengerDto getPassenger() {
        return PassengerDto.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerProfileDto getPassengerProfile() {
        return PassengerProfileDto.builder()
                .passenger(getPassenger())
                .rating(DEFAULT_PASSENGER_RATING)
                .build();
    }

    public NewPassengerDto getRequestForSavePassenger() {
        return NewPassengerDto.builder()
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .password(DEFAULT_PASSENGER_PASSWORD)
                .build();
    }

    public NewPassengerDto getRequestForSavePassengerWithInvalidLastName() {
        return NewPassengerDto.builder()
                .lastname("Iva 56nov")
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .password(DEFAULT_PASSENGER_PASSWORD)
                .build();
    }

    public NewPassengerDto getRequestForSavePassengerWithInvalidFirstName() {
        return NewPassengerDto.builder()
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname("Iv35an")
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .password(DEFAULT_PASSENGER_PASSWORD)
                .build();
    }

    public NewPassengerDto getRequestForSavePassengerWithInvalidEmail() {
        return NewPassengerDto.builder()
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email("mr.ivanov.ru")
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .password(DEFAULT_PASSENGER_PASSWORD)
                .build();
    }

    public NewPassengerDto getRequestForSavePassengerWithInvalidPhoneNumber() {
        return NewPassengerDto.builder()
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber("3752r93660893")
                .password(DEFAULT_PASSENGER_PASSWORD)
                .build();
    }

    public NewPassengerDto getRequestForSavePassengerWithInvalidPassword() {
        return NewPassengerDto.builder()
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .password("")
                .build();
    }

    public PassengerDto getRequestForEditPassenger() {
        return PassengerDto.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerDto getRequestForEditPassengerWithInvalidLastname() {
        return PassengerDto.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname("%gghh7")
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerDto getRequestForEditPassengerWithInvalidFirstname() {
        return PassengerDto.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname("%gghh7")
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerDto getRequestForEditPassengerWithInvalidEmail() {
        return PassengerDto.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email("%gghh7")
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .build();
    }

    public PassengerDto getRequestForEditPassengerWithInvalidPhoneNumber() {
        return PassengerDto.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .phoneNumber("%gghh7")
                .build();
    }
}
