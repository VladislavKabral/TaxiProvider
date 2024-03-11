package by.modsen.taxiprovider.driverservice.model.driver;

import by.modsen.taxiprovider.driverservice.model.rating.DriverRating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverProfile {

    private Driver driver;

    private DriverRating rating;
}
