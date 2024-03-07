package by.modsen.taxiprovider.ridesservice.dto.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    @NotNull(message = "Latitude must be not empty")
    @Min(value = -90, message = "Minimal value for latitude is '-90'")
    @Max(value = 90, message = "Maximum value for latitude is '90'")
    @JsonProperty("lat")
    private Double latitude;

    @NotNull(message = "Longitude must be not empty")
    @Min(value = -180, message = "Minimal value for longitude is '-180'")
    @Max(value = 180, message = "Maximum value for longitude is '180'")
    @JsonProperty("lon")
    private Double longitude;
}
