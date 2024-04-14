package by.modsen.taxiprovider.passengerservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingResponseDto {

    private long taxiUserId;

    private String role;

    private double value;
}
