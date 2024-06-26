package by.modsen.taxiprovider.driverservice.dto.rating;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import static by.modsen.taxiprovider.driverservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDto {

    @NotNull(message = DRIVER_ID_IS_NULL)
    @Min(value = 1, message = DRIVER_ID_MINIMAL_VALUE_IS_INVALID)
    private long taxiUserId;

    @NotNull(message = DRIVER_ROLE_IS_EMPTY)
    private String role;

    private double value;
}
