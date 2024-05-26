package by.modsen.taxiprovider.paymentservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideResponseDto {

    private long id;

    private long driverId;

    private long passengerId;
}
