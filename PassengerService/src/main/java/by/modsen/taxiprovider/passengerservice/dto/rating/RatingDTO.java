package by.modsen.taxiprovider.passengerservice.dto.rating;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    @NotNull(message = "Driver's id must be not null")
    @Min(value = 1, message = "Minimal value of driver's id is '1'")
    private long taxiUserId;

    @NotNull(message = "Driver's role must be not empty")
    private String role;

    private double value;
}
