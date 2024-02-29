package by.modsen.taxiprovider.ridesservice.model.ride;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
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
public class PotentialRide {

    @NotNull(message = "Source address must be not empty")
    private Address sourceAddress;

    @NotNull(message = "Target address-(es) must be not empty")
    @Size(min = 1, message = "Must be at least one target address")
    private List<Address> targetAddresses;

    private PromoCode promoCode;
}
