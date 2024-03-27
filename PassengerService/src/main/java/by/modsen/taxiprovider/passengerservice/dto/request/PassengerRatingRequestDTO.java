package by.modsen.taxiprovider.passengerservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerRatingRequestDTO {

    @NotNull(message = PASSENGER_ID_IS_NULL)
    @Min(value = 1, message = PASSENGER_ID_MINIMAL_VALUE_IS_INVALID)
    private long taxiUserId;

    @NotBlank(message = PASSENGER_ROLE_IS_EMPTY)
    private String role;
}
