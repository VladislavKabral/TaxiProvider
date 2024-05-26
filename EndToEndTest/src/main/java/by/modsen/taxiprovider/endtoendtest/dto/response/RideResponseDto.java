package by.modsen.taxiprovider.endtoendtest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideResponseDto {

    private long id;

    private long driverId;

    private long passengerId;
}
