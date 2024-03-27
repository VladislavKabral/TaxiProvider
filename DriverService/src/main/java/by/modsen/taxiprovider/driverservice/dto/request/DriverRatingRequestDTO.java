package by.modsen.taxiprovider.driverservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class DriverRatingRequestDTO {

    @NotNull(message = DRIVER_ID_IS_NULL)
    @Min(value = 1, message = DRIVER_ID_MINIMAL_VALUE_IS_INVALID)
    private long taxiUserId;

    @NotBlank(message = DRIVER_ROLE_IS_EMPTY)
    private String role;
}
