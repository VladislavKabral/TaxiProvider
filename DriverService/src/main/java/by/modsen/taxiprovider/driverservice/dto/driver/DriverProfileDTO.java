package by.modsen.taxiprovider.driverservice.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfileDTO {

    private DriverDTO driver;

    private Double rating;
}