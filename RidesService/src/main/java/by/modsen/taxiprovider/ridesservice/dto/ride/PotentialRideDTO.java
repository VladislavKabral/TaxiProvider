package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.address.AddressDTO;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PotentialRideDTO {

    @NotNull(message = "Source address must be not empty")
    private AddressDTO sourceAddress;

    @NotNull(message = "Target address-(es) must be not empty")
    private List<AddressDTO> targetAddresses;

    private PromoCode promoCode;
}
