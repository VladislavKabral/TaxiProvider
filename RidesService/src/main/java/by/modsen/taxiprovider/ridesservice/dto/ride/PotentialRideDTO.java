package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PotentialRideDTO {

    @Valid
    @NotNull(message = "Source address must be not empty")
    private AddressDTO sourceAddress;

    @Valid
    @NotNull(message = "Target address-(es) must be not empty")
    @Size(min = 1, message = "Must be at least one target address")
    private List<AddressDTO> targetAddresses;

    @Valid
    private PromoCodeDTO promoCode;
}
