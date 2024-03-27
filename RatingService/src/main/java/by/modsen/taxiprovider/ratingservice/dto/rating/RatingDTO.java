package by.modsen.taxiprovider.ratingservice.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {

    private long id;

    @NotNull(message = "Taxi user's id must be not null")
    @Min(value = 1, message = "Minimal value for taxi user's id id '1'")
    private long taxiUserId;

    @NotBlank(message = "Taxi user's role must be not empty")
    private String role;

    @NotNull(message = "Value of rating must be not empty")
    @Min(value = 1, message = "Minimal value for the rating's value is '1'")
    @Max(value = 5, message = "Maximum value for the rating's value is '5'")
    private int value;
}
