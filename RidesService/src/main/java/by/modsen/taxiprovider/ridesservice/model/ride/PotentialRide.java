package by.modsen.taxiprovider.ridesservice.model.ride;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PotentialRide {

    private Point sourcePoint;

    private List<Point> targetAddresses;

    private PromoCode promoCode;
}
