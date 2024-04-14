package by.modsen.taxiprovider.endtoendtest.dto.response;

import by.modsen.taxiprovider.endtoendtest.dto.ride.RideDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RidesListResponseDto {

    private List<RideDto> content;
}
