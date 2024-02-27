package by.modsen.taxiprovider.ridesservice.dto.ride.address;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationAddressDTO {

    @NotNull(message = "Address must be not empty")
    private AddressDTO address;
}
