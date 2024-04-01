package by.modsen.taxiprovider.ridesservice.dto.ride;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistanceRequestDto {

    private List<AddressDto> points;

    private int[] sources;

    private int[] targets;
}
