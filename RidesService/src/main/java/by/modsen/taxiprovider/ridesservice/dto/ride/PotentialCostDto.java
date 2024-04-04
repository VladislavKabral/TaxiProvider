package by.modsen.taxiprovider.ridesservice.dto.ride;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PotentialCostDto {

    private BigDecimal cost;
}
