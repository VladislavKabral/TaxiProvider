package by.modsen.taxiprovider.driverservice.utilily;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.driverservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.driverservice.model.Driver;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class DriversTestUtil {

    public final String GET_DRIVERS_PATH = "/drivers";
    public final String GET_FREE_DRIVERS_PATH = "/drivers/free";
    public final String GET_DEFAULT_DRIVER_PATH = "/drivers/1";
    public final String GET_DEFAULT_DRIVER_PROFILE_PATH = "/drivers/1/profile";
    public final String GET_DEFAULT_NON_EXISTS_DRIVER_PROFILE_PATH = "/drivers/4/profile";
    public final String DEACTIVATE_DEFAULT_DRIVER_PATH = "/drivers/1";
    public final String GET_NON_EXISTENT_DRIVER_PATH = "/drivers/4";
    public final String DEACTIVATE_NON_EXISTENT_DRIVER_PATH = "/drivers/4";
    public final String INIT_DRIVER_RATING_PATH = "/ratings/init";
    public final String GET_DRIVER_RATING_PATH = "/ratings?taxiUserId=1&role=DRIVER";
    public final String PAGE_PARAM_NAME = "page";
    public final String SIZE_PARAM_NAME = "size";
    public final String SORT_PARAM_NAME = "sort";
    public final long DEFAULT_DRIVER_ID = 1;
    public final long DEFAULT_INVALID_DRIVER_ID = 4;
    public final String DEFAULT_SORT_FIELD = "lastname";
    public final int DEFAULT_PAGE = 1;
    public final int DEFAULT_INCORRECT_PAGE = 10;
    public final int DEFAULT_PAGE_SIZE = 3;
    public final int DEFAULT_INVALID_PAGE = -1;
    public final int DEFAULT_INVALID_PAGE_SIZE = 0;
    public final double DEFAULT_DRIVER_RATING = 5.0;
    public final String DEFAULT_DRIVER_LASTNAME = "Vasiliev";
    public final String DEFAULT_NEW_DRIVER_LASTNAME = "Leonov";
    public final String DEFAULT_DRIVER_FIRSTNAME = "Platon";
    public final String DEFAULT_NEW_DRIVER_FIRSTNAME = "Kiril";
    public final String DEFAULT_DRIVER_EMAIL = "mr.vasiliev@mail.ru";
    public final String DEFAULT_NEW_DRIVER_EMAIL = "mr.leonov@mail.ru";
    public final String DEFAULT_DRIVER_PHONE_NUMBER = "+375293660893";
    public final String DEFAULT_NEW_DRIVER_PHONE_NUMBER = "+375292796152";
    public final String DEFAULT_DRIVER_ACCOUNT_STATUS = "ACTIVE";
    public final String DRIVER_ROLE_NAME = "DRIVER";
    public final String DEFAULT_DRIVER_STATUS = "FREE";
    public final BigDecimal DEFAULT_DRIVER_BALANCE = BigDecimal.valueOf(0.0);
    public final String DEFAULT_NEW_DRIVER_PASSWORD = "$2a$12$b7CcS8TDc.0Zjc2bZHYFPOfnOvJsR6EDC.PlDloRe3RevAC3jYLDS";

    public DriverListDto getDrivers() {
        return DriverListDto.builder()
                .content(List.of(DriverDto.builder()
                                .id(1)
                                .lastname("Vasiliev")
                                .firstname("Platon")
                                .email("mr.vasiliev@mail.ru")
                                .phoneNumber("+375293660893")
                                .accountStatus("ACTIVE")
                                .status("FREE")
                                .balance(BigDecimal.valueOf(0.00))
                                .build(),
                        DriverDto.builder()
                                .id(2)
                                .lastname("Dybrovin")
                                .firstname("Ilia")
                                .email("mr.dybrovin@mail.ru")
                                .phoneNumber("+375296499224")
                                .accountStatus("ACTIVE")
                                .status("FREE")
                                .balance(BigDecimal.ZERO)
                                .build(),
                        DriverDto.builder()
                                .id(3)
                                .lastname("Smirnov")
                                .firstname("Sergei")
                                .email("mr.smirnov@mail.ru")
                                .phoneNumber("+375298415692")
                                .accountStatus("ACTIVE")
                                .status("FREE")
                                .balance(BigDecimal.valueOf(0.00))
                                .build()))
                .build();
    }

    public List<Driver> getDriverList() {
        return List.of(Driver.builder()
                        .id(1)
                        .lastname("Vasiliev")
                        .firstname("Platon")
                        .email("mr.vasiliev@mail.ru")
                        .phoneNumber("+375293660893")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.valueOf(0.00))
                        .build(),
                Driver.builder()
                        .id(2)
                        .lastname("Dybrovin")
                        .firstname("Ilia")
                        .email("mr.dybrovin@mail.ru")
                        .phoneNumber("+375296499224")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.ZERO)
                        .build(),
                Driver.builder()
                        .id(3)
                        .lastname("Smirnov")
                        .firstname("Sergei")
                        .email("mr.smirnov@mail.ru")
                        .phoneNumber("+375298415692")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.valueOf(0.00))
                        .build());
    }

    public DriverListDto getEmptyDriverList() {
        return new DriverListDto(new ArrayList<>());
    }

    public DriverDto getDriver() {
        return DriverDto.builder()
                .id(DEFAULT_DRIVER_ID)
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .accountStatus(DEFAULT_DRIVER_ACCOUNT_STATUS)
                .status(DEFAULT_DRIVER_STATUS)
                .balance(DEFAULT_DRIVER_BALANCE)
                .build();
    }

    public Driver getDefaultDriver() {
        return Driver.builder()
                .id(DEFAULT_DRIVER_ID)
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .accountStatus(DEFAULT_DRIVER_ACCOUNT_STATUS)
                .status(DEFAULT_DRIVER_STATUS)
                .balance(DEFAULT_DRIVER_BALANCE)
                .build();
    }

    public DriverProfileDto getDriverProfile() {
        return DriverProfileDto.builder()
                .driver(getDriver())
                .rating(DEFAULT_DRIVER_RATING)
                .build();
    }

    public RatingDto getDefaultDriverRating() {
        return RatingDto.builder()
                .taxiUserId(DEFAULT_DRIVER_ID)
                .value(DEFAULT_DRIVER_RATING)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public TaxiUserRatingDto getDefaultDriverTaxiUserRating() {
        return TaxiUserRatingDto.builder()
                .taxiUserId(DEFAULT_DRIVER_ID)
                .value(DEFAULT_DRIVER_RATING)
                .role(DRIVER_ROLE_NAME)
                .build();
    }

    public NewDriverDto getRequestForSaveDriver() {
        return NewDriverDto.builder()
                .lastname(DEFAULT_NEW_DRIVER_LASTNAME)
                .firstname(DEFAULT_NEW_DRIVER_FIRSTNAME)
                .email(DEFAULT_NEW_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_NEW_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_NEW_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDto getRequestForSaveDriverWithInvalidLastName() {
        return NewDriverDto.builder()
                .lastname("Va42 siliev")
                .firstname(DEFAULT_NEW_DRIVER_FIRSTNAME)
                .email(DEFAULT_NEW_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_NEW_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_NEW_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDto getRequestForSaveDriverWithInvalidFirstName() {
        return NewDriverDto.builder()
                .lastname(DEFAULT_NEW_DRIVER_LASTNAME)
                .firstname("Plat35on")
                .email(DEFAULT_NEW_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_NEW_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_NEW_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDto getRequestForSaveDriverWithInvalidEmail() {
        return NewDriverDto.builder()
                .lastname(DEFAULT_NEW_DRIVER_LASTNAME)
                .firstname(DEFAULT_NEW_DRIVER_FIRSTNAME)
                .email("mr.vasiliev.ru")
                .phoneNumber(DEFAULT_NEW_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_NEW_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDto getRequestForSaveDriverWithInvalidPhoneNumber() {
        return NewDriverDto.builder()
                .lastname(DEFAULT_NEW_DRIVER_LASTNAME)
                .firstname(DEFAULT_NEW_DRIVER_FIRSTNAME)
                .email(DEFAULT_NEW_DRIVER_EMAIL)
                .phoneNumber("3752r93660893")
                .password(DEFAULT_NEW_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDto getRequestForSaveDriverWithInvalidPassword() {
        return NewDriverDto.builder()
                .lastname(DEFAULT_NEW_DRIVER_LASTNAME)
                .firstname(DEFAULT_NEW_DRIVER_FIRSTNAME)
                .email(DEFAULT_NEW_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_NEW_DRIVER_PHONE_NUMBER)
                .password("")
                .build();
    }

    public DriverDto getRequestForUpdateDriverWithInvalidStatus() {
        return DriverDto.builder()
                .id(DEFAULT_DRIVER_ID)
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .accountStatus(DEFAULT_DRIVER_ACCOUNT_STATUS)
                .status("lwekljwf")
                .balance(DEFAULT_DRIVER_BALANCE)
                .build();
    }
}
