package by.modsen.taxiprovider.ratingservice.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxiUserRatingDTO {

    private long taxiUserId;

    private String role;

    private Double value;
}
