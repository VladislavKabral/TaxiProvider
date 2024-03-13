package by.modsen.taxiprovider.driverservice.dto.driver;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRequestDTO {

    @NotNull(message = "Driver's id must be not null")
    @Min(value = 1, message = "Minimal value for driver's id is '1'")
    private long driverId;

    @NotBlank(message = "Ride's status must be not empty")
    private String rideStatus;
}
