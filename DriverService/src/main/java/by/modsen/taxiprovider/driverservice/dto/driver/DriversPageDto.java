package by.modsen.taxiprovider.driverservice.dto.driver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriversPageDto {

    private List<DriverDto> content;

    private int page;

    private int size;
}
