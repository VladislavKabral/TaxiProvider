package by.modsen.taxiprovider.ridesservice.dto.ride.address;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    @NotNull(message = "Latitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong latitude format")
    private String lat;

    @NotNull(message = "Longitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong longitude format")
    private String lon;
}
