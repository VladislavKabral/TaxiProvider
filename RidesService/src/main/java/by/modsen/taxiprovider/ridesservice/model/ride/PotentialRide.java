package by.modsen.taxiprovider.ridesservice.model.ride;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
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

    private Address sourceAddress;

    private List<Address> destinationAddresses;

    private PromoCode promoCode;
}
