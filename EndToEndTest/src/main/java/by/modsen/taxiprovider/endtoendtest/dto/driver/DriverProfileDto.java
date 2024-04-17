package by.modsen.taxiprovider.endtoendtest.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfileDto {

    private DriverDto driver;

    private Double rating;
}
