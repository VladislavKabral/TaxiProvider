package by.modsen.taxiprovider.passengerservice.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxiUserRatingDto {

    private long taxiUserId;

    private String role;

    private Double value;
}
