package by.modsen.taxiprovider.driverservice.utilily;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class DriversTestUtil {

    public final long DEFAULT_DRIVER_ID = 1;
    public final String DEFAULT_SORT_FIELD = "lastname";
    public final int DEFAULT_PAGE = 1;
    public final int DEFAULT_PAGE_SIZE = 3;
    public final int DEFAULT_INVALID_PAGE = -1;
    public final int DEFAULT_INVALID_PAGE_SIZE = 0;
    public final double DEFAULT_DRIVER_RATING = 5.0;
    public final String DEFAULT_DRIVER_LASTNAME = "Vasiliev";
    public final String DEFAULT_DRIVER_FIRSTNAME = "Platon";
    public final String DEFAULT_DRIVER_EMAIL = "mr.vasiliev@mail.ru";
    public final String DEFAULT_DRIVER_PHONE_NUMBER = "+375293660893";
    public final String DEFAULT_DRIVER_ACCOUNT_STATUS = "ACTIVE";
    public final String DEFAULT_DRIVER_STATUS = "FREE";
    public final BigDecimal DEFAULT_DRIVER_BALANCE = BigDecimal.valueOf(28.00);
    public final String DEFAULT_DRIVER_PASSWORD = "$2a$12$b7CcS8TDc.0Zjc2bZGAZPOfnOvJsR6EDC.PlDloRe3RevAC3jYLDS";

    public List<DriverDTO> getDrivers() {
        return List.of(DriverDTO.builder()
                        .id(1)
                        .lastname("Vasiliev")
                        .firstname("Platon")
                        .email("mr.vasiliev@mail.ru")
                        .phoneNumber("+375293660893")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.valueOf(28.00))
                .build(),
                DriverDTO.builder()
                        .id(2)
                        .lastname("Dybrovin")
                        .firstname("Ilia")
                        .email("mr.dybrovin@mail.ru")
                        .phoneNumber("+375296499224")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.ZERO)
                        .build(),
                DriverDTO.builder()
                        .id(3)
                        .lastname("Smirnov")
                        .firstname("Sergei")
                        .email("mr.smirnov@mail.ru")
                        .phoneNumber("+375298415692")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.valueOf(21.00))
                        .build());
    }

    public List<DriverDTO> getSortedDrivers() {
        return List.of(DriverDTO.builder()
                        .id(2)
                        .lastname("Dybrovin")
                        .firstname("Ilia")
                        .email("mr.dybrovin@mail.ru")
                        .phoneNumber("+375296499224")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.ZERO)
                        .build(),
                DriverDTO.builder()
                        .id(3)
                        .lastname("Smirnov")
                        .firstname("Sergei")
                        .email("mr.smirnov@mail.ru")
                        .phoneNumber("+375298415692")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.valueOf(21.00))
                        .build(),
                DriverDTO.builder()
                        .id(1)
                        .lastname("Vasiliev")
                        .firstname("Platon")
                        .email("mr.vasiliev@mail.ru")
                        .phoneNumber("+375293660893")
                        .accountStatus("ACTIVE")
                        .status("FREE")
                        .balance(BigDecimal.valueOf(28.00))
                        .build());
    }

    public DriverDTO getDriver() {
        return DriverDTO.builder()
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

    public DriverProfileDTO getDriverProfile() {
        return DriverProfileDTO.builder()
                .driver(getDriver())
                .rating(DEFAULT_DRIVER_RATING)
                .build();
    }

    public NewDriverDTO getRequestForSaveDriver() {
        return NewDriverDTO.builder()
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDTO getRequestForSaveDriverWithInvalidLastName() {
        return NewDriverDTO.builder()
                .lastname("Va42 siliev")
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDTO getRequestForSaveDriverWithInvalidFirstName() {
        return NewDriverDTO.builder()
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname("Plat35on")
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDTO getRequestForSaveDriverWithInvalidEmail() {
        return NewDriverDTO.builder()
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email("mr.vasiliev.ru")
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .password(DEFAULT_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDTO getRequestForSaveDriverWithInvalidPhoneNumber() {
        return NewDriverDTO.builder()
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber("3752r93660893")
                .password(DEFAULT_DRIVER_PASSWORD)
                .build();
    }

    public NewDriverDTO getRequestForSaveDriverWithInvalidPassword() {
        return NewDriverDTO.builder()
                .lastname(DEFAULT_DRIVER_LASTNAME)
                .firstname(DEFAULT_DRIVER_FIRSTNAME)
                .email(DEFAULT_DRIVER_EMAIL)
                .phoneNumber(DEFAULT_DRIVER_PHONE_NUMBER)
                .password("")
                .build();
    }

    public DriverDTO getRequestForUpdateDriverWithInvalidStatus() {
        return DriverDTO.builder()
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
