package by.modsen.taxiprovider.driverservice.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {

    @NotNull(message = "Rating's value must be not empty")
    @Min(value = 1, message = "Minimal value of rating is '1'")
    @Max(value = 5, message = "Maximum value of rating is '5'")
    private int value;
}
