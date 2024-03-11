package by.modsen.taxiprovider.driverservice.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreeDriverDTO {

    private long id;

    private String lastname;

    private String firstname;
}
