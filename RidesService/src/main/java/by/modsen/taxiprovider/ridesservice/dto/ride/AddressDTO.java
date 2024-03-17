package by.modsen.taxiprovider.ridesservice.dto.ride;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {

    private static final int LATITUDE_MINIMUM_VALUE = -90;

    private static final int LATITUDE_MAXIMUM_VALUE = 90;

    private static final int LONGITUDE_MINIMUM_VALUE = -180;

    private static final int LONGITUDE_MAXIMUM_VALUE = 180;

    @NotNull(message = ADDRESS_LATITUDE_IS_NULL)
    @Min(value = LATITUDE_MINIMUM_VALUE, message = LATITUDE_MINIMAL_VALUE_IS_INVALID)
    @Max(value = LATITUDE_MAXIMUM_VALUE, message = LATITUDE_MAXIMUM_VALUE_IS_INVALID)
    @JsonProperty("lat")
    private Double latitude;

    @NotNull(message = ADDRESS_LONGITUDE_IS_NULL)
    @Min(value = LONGITUDE_MINIMUM_VALUE, message = LONGITUDE_MINIMAL_VALUE_IS_INVALID)
    @Max(value = LONGITUDE_MAXIMUM_VALUE, message = LONGITUDE_MAXIMUM_VALUE_IS_INVALID)
    @JsonProperty("lon")
    private Double longitude;
}
