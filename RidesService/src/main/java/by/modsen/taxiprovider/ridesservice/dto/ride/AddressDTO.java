package by.modsen.taxiprovider.ridesservice.dto.ride;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = -90, message = "Minimal value for latitude is '-90'")
    @Max(value = 90, message = "Maximum value for latitude is '90'")
    private String latitude;

    @NotNull(message = "Longitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong longitude format")
    @Min(value = -180, message = "Minimal value for longitude is '-180'")
    @Max(value = 180, message = "Maximum value for longitude is '180'")
    private String longitude;
}
