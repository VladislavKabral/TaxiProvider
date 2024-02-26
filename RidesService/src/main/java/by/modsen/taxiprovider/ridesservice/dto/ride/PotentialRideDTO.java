package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PotentialRideDTO {

    private Address sourceAddress;

    private List<Address> targetAddresses;

    private PromoCode promoCode;
}
