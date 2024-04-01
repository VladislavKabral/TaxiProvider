package by.modsen.taxiprovider.ratingservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponseDTO {

    private long taxiUserId;

    private String role;

    private double value;
}
