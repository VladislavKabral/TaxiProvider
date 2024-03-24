package by.modsen.taxiprovider.ratingservice.dto.rating;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.ratingservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDTO {

    private long id;

    @NotNull(message = TAXI_USER_ID_IS_NULL)
    @Min(value = 1, message = TAXI_USER_ID_MINIMAL_VALUE)
    private long taxiUserId;

    @NotBlank(message = TAXI_USER_ROLE_IS_EMPTY)
    private String role;

    @NotNull(message = RATING_VALUE_IS_EMPTY)
    @Min(value = 1, message = RATING_MINIMAL_VALUE_IS_INVALID)
    @Max(value = 5, message = RATING_MAXIMUM_VALUE_IS_INVALID)
    private int value;
}
