package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import by.modsen.taxiprovider.ridesservice.model.ride.address.DestinationAddress;
import by.modsen.taxiprovider.ridesservice.repository.ride.RidesRepository;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.address.AddressesService;
import by.modsen.taxiprovider.ridesservice.service.ride.address.DestinationAddressesService;
import by.modsen.taxiprovider.ridesservice.service.ride.distance.DistanceCalculator;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RidesService {

    private final RidesRepository ridesRepository;

    private final PromoCodesService promoCodesService;

    private final DistanceCalculator distanceCalculator;

    private final AddressesService addressesService;

    private final DestinationAddressesService destinationAddressesService;

    private static final int MINIMAL_COUNT_OF_TARGET_ADDRESSES = 1;

    private static final double STARTING_COST = 3.0;

    private static final double COST_OF_KILOMETER = 0.5;


    public List<Ride> findAll() throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findAll();

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("There aren't any rides");
        }

        return rides;
    }

    @Transactional
    public void save(Ride ride, PromoCode promoCode) throws IOException, ParseException, DistanceCalculationException, EntityNotFoundException, InterruptedException {
        Address sourceAddress = ride.getSourceAddress();
        addressesService.save(sourceAddress);

        List<DestinationAddress> destinationAddresses = ride.getDestinationAddresses();
        List<Address> targetAddresses = new ArrayList<>();
        for (DestinationAddress destinationAddress: destinationAddresses) {
            addressesService.save(destinationAddress.getAddress());
            destinationAddress.setRide(ride);
            targetAddresses.add(destinationAddress.getAddress());
        }

        double rideCost = calculatePotentialRideCost(PotentialRide.builder()
                .sourceAddress(sourceAddress)
                .targetAddresses(targetAddresses)
                .promoCode(promoCode)
                .build());

        ride.setCost(rideCost);
        ridesRepository.save(ride);

        destinationAddressesService.save(destinationAddresses);
    }

    public double calculatePotentialRideCost(PotentialRide potentialRide) throws IOException,
            ParseException, DistanceCalculationException, InterruptedException, EntityNotFoundException {

        int distance = getRideDistance(potentialRide);
        PromoCode promoCode = potentialRide.getPromoCode();
        double discount = 1.0;
        if (promoCode != null) {
            discount = promoCodesService.findByValue(promoCode.getValue()).getDiscount();
        }

        return (STARTING_COST +  (double) (distance / 1000) * COST_OF_KILOMETER) * discount;
    }

    private int getRideDistance(PotentialRide potentialRide) throws IOException,
            ParseException, InterruptedException, DistanceCalculationException {

        List<Address> targetAddresses = potentialRide.getTargetAddresses();

        int rideDistance =  distanceCalculator.calculate(
                potentialRide.getSourceAddress(),
                targetAddresses.get(0)
        );

        if (targetAddresses.size() != MINIMAL_COUNT_OF_TARGET_ADDRESSES) {
            for (int i = 0; i < targetAddresses.size() - 1; i++) {
                rideDistance += distanceCalculator.calculate(targetAddresses.get(i), targetAddresses.get(i + 1));
            }
        }

        return rideDistance;
    }

}
