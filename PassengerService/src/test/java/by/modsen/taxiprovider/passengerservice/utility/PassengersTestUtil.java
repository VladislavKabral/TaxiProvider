package by.modsen.taxiprovider.passengerservice.utility;

import by.modsen.taxiprovider.passengerservice.dto.passenger.NewPassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerListDto;
import by.modsen.taxiprovider.passengerservice.dto.passenger.PassengerProfileDto;
import by.modsen.taxiprovider.passengerservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.passengerservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.passengerservice.model.Passenger;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PassengersTestUtil {

    public final String GET_PASSENGERS_PATH = "/passengers";
    public final String GET_DEFAULT_PASSENGER_PATH = "/passengers/1";
    public final String GET_DEFAULT_PASSENGER_PROFILE_PATH = "/passengers/1/profile";
    public final String GET_DEFAULT_NON_EXISTS_PASSENGER_PROFILE_PATH = "/passengers/4/profile";
    public final String GET_NON_EXISTENT_PASSENGER_PATH = "/passengers/4";
    public final String DEACTIVATE_DEFAULT_PASSENGER_PATH = "/passengers/1";
    public final String DEACTIVATE_NON_EXISTENT_PASSENGER_PATH = "/passengers/4";
    public final String INIT_PASSENGER_RATING_PATH = "/ratings/init";
    public final String GET_PASSENGER_RATING_PATH = "/ratings?taxiUserId=1&role=PASSENGER";
    public final long DEFAULT_PASSENGER_ID = 1;
    public final long DEFAULT_INVALID_PASSENGER_ID = 4;
    public final int DEFAULT_INCORRECT_PAGE = 10;
    public final String DEFAULT_SORT_FIELD = "lastname";
    public final String PAGE_PARAM_NAME = "page";
    public final String SIZE_PARAM_NAME = "size";
    public final String SORT_PARAM_NAME = "sort";
    public final int DEFAULT_PAGE = 1;
    public final int DEFAULT_PAGE_SIZE = 3;
    public final int DEFAULT_INVALID_PAGE = -1;
    public final int DEFAULT_INVALID_PAGE_SIZE = 0;
    public final double DEFAULT_PASSENGER_RATING = 5.0;
    public final String DEFAULT_PASSENGER_LASTNAME = "Ivanov";
    public final String DEFAULT_NEW_PASSENGER_LASTNAME = "Leonov";
    public final String DEFAULT_PASSENGER_FIRSTNAME = "Ivan";
    public final String DEFAULT_NEW_PASSENGER_FIRSTNAME = "Kiril";
    public final String DEFAULT_PASSENGER_EMAIL = "mr.ivanov@mail.ru";
    public final String DEFAULT_NEW_PASSENGER_EMAIL = "mr.leonov@mail.ru";
    public final String DEFAULT_PASSENGER_PHONE_NUMBER = "+375291234590";
    public final String DEFAULT_NEW_PASSENGER_PHONE_NUMBER = "+375292796152";
    public final String DEFAULT_PASSENGER_PASSWORD = "$2a$12$kjMvQl359in5BXoX1mgAhORCAwfCU3OREAAR1wX9444WVoMvORKiW";
    public final String DEFAULT_PASSENGER_STATUS = "ACTIVE";
    public final String PASSENGER_ROLE_NAME = "PASSENGER";

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

    public PassengerListDto getPassengersListDto() {
        return PassengerListDto.builder()
                .content(getPassengers())
                .build();
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
                .lastname(DEFAULT_NEW_PASSENGER_LASTNAME)
                .firstname(DEFAULT_NEW_PASSENGER_FIRSTNAME)
                .email(DEFAULT_NEW_PASSENGER_EMAIL)
                .phoneNumber(DEFAULT_NEW_PASSENGER_PHONE_NUMBER)
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

    public List<Passenger> getPassengersList() {
        return List.of(
                Passenger.builder()
                        .id(1)
                        .lastname("Ivanov")
                        .firstname("Ivan")
                        .email("mr.ivanov@mail.ru")
                        .password("$2a$12$kjMvQl359in5BXoX1mgAhORCAwfCU3OREAAR1wX9444WVoMvORKiW")
                        .role("PASSENGER")
                        .phoneNumber("+375291234590")
                        .status("ACTIVE")
                .build(),
                Passenger.builder()
                        .id(2)
                        .lastname("Petrov")
                        .firstname("Petr")
                        .email("mr.petrov@mail.ru")
                        .password("$2a$12$apAei/5tTIhOHrwP79oD8.E7F6W/qdvq3XdQn31CFkGMMnotThDXC")
                        .role("PASSENGER")
                        .phoneNumber("+375336894867")
                        .status("ACTIVE")
                        .build(),
                Passenger.builder()
                        .id(3)
                        .lastname("Borisov")
                        .firstname("Boris")
                        .email("mr.borisov@mail.ru")
                        .password("$2a$12$WNEOZ9NfIYzdnK/fP1IKl.NUiFUUg7iAmeuiMmg0FT4.WN4fYMhcC")
                        .role("PASSENGER")
                        .phoneNumber("+375296131124")
                        .status("ACTIVE")
                        .build()
        );
    }

    public Passenger getDefaultPassenger() {
        return Passenger.builder()
                .id(DEFAULT_PASSENGER_ID)
                .lastname(DEFAULT_PASSENGER_LASTNAME)
                .firstname(DEFAULT_PASSENGER_FIRSTNAME)
                .email(DEFAULT_PASSENGER_EMAIL)
                .password(DEFAULT_PASSENGER_PASSWORD)
                .role(PASSENGER_ROLE_NAME)
                .phoneNumber(DEFAULT_PASSENGER_PHONE_NUMBER)
                .status(DEFAULT_PASSENGER_STATUS)
                .build();
    }

    public RatingDto getDefaultDriverRating() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_PASSENGER_ID)
                .value(DEFAULT_PASSENGER_RATING)
                .role(PASSENGER_ROLE_NAME)
                .build();
    }

    public TaxiUserRatingDto getDefaultTaxiUserRating() {
        return TaxiUserRatingDto.builder()
                .taxiUserId(DEFAULT_PASSENGER_ID)
                .value(DEFAULT_PASSENGER_RATING)
                .role(PASSENGER_ROLE_NAME)
                .build();
    }
}
