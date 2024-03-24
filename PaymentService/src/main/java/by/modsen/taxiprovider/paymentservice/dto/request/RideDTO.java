package by.modsen.taxiprovider.paymentservice.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideDTO {

    @Min(value = 1, message = PASSENGER_ID_IS_INVALID)
    private long passengerId;

    @Min(value = 1, message = DRIVER_ID_IS_INVALID)
    private long driverId;

    private String status;
}
