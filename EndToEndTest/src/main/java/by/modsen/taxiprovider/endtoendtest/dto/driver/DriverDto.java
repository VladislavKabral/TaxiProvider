package by.modsen.taxiprovider.endtoendtest.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDto {

    private long id;

    private String lastname;

    private String firstname;

    private String email;

    private String phoneNumber;

    private String accountStatus;

    private String status;

    private BigDecimal balance;
}
