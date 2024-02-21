package by.modsen.taxiprovider.passengerservice.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    @NotNull(message = "Rating's value must be not empty")
    @Min(value = 1, message = "Minimal value of rating must be '1'")
    @Max(value = 5, message = "Maximum value of rating must be '5'")
    private int value;
}
