package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.model.ride.PotentialRide;
import by.modsen.taxiprovider.ridesservice.repository.ride.RidesRepository;
import by.modsen.taxiprovider.ridesservice.service.promocode.PromoCodesService;
import by.modsen.taxiprovider.ridesservice.service.ride.distance.DistanceCalculator;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class RidesService {

    private final RidesRepository ridesRepository;

    private final PromoCodesService promoCodesService;

    private final DistanceCalculator distanceCalculator;

    private final AddressesService addressesService;

    private static final int MINIMAL_COUNT_OF_TARGET_ADDRESSES = 1;

    private static final double STARTING_COST = 3.0;

    private static final double COST_OF_KILOMETER = 0.5;

    private static final int METERS_IN_KILOMETER = 1000;

    public List<Ride> findAll() throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findAll();

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("There aren't any rides");
        }

        return rides;
    }

    public Ride findById(long id) throws EntityNotFoundException {
        Optional<Ride> ride = ridesRepository.findById(id);

        return ride.orElseThrow(EntityNotFoundException.entityNotFoundException("Ride with id '" + id + "' wasn't found"));
    }

    public List<Ride> findByPassengerId(long passengerId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByPassengerId(passengerId);

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("Passenger with id '" + passengerId + "' doesn't have any rides");
        }

        return rides.stream()
                .filter(ride -> ride.getStatus().equals("Active"))
                .collect(Collectors.toList());
    }

    public List<Ride> findByDriverId(long driverId) throws EntityNotFoundException {
        List<Ride> rides = ridesRepository.findByDriverId(driverId);

        if (rides.isEmpty()) {
            throw new EntityNotFoundException("Driver with id '" + driverId + "' doesn't have any rides");
        }

        return rides.stream()
                .filter(ride -> ride.getStatus().equals("Active"))
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(Ride ride, PromoCode promoCode) throws IOException, ParseException, DistanceCalculationException,
            EntityNotFoundException, InterruptedException {
        Address sourceAddress = ride.getSourceAddress();

        Address existingAddress = addressesService
                .findByLatitudeAndLongitude(sourceAddress.getLatitude(), sourceAddress.getLongitude());
        if (existingAddress == null) {
            addressesService.save(sourceAddress);
        } else {
            ride.setSourceAddress(existingAddress);
        }

        List<Address> destinationAddresses = ride.getDestinationAddresses();
        for (int i = 0; i < destinationAddresses.size(); i++) {
            Address address = addressesService
                    .findByLatitudeAndLongitude(destinationAddresses.get(i).getLatitude(),
                            destinationAddresses.get(i).getLongitude());
            if (address == null) {
                addressesService.save(destinationAddresses.get(i));
            } else {
                destinationAddresses.set(i, address);
            }

            ride.setDestinationAddresses(destinationAddresses);
        }

        double rideCost = calculatePotentialRideCost(PotentialRide.builder()
                .sourceAddress(sourceAddress)
                .targetAddresses(destinationAddresses)
                .promoCode(promoCode)
                .build());

        ride.setCost(rideCost);
        ride.setStatus("Active");
        ridesRepository.save(ride);
    }

    @Transactional
    public void deactivate(long id) throws EntityNotFoundException {
        Ride ride = findById(id);

        ride.setStatus("Deleted");

        ridesRepository.save(ride);
    }

    public double calculatePotentialRideCost(PotentialRide potentialRide) throws IOException,
            ParseException, DistanceCalculationException, InterruptedException, EntityNotFoundException {

        int distance = getRideDistance(potentialRide);
        PromoCode promoCode = potentialRide.getPromoCode();
        double discount = 0.0;
        if (promoCode != null) {
            discount = promoCodesService.findByValue(promoCode.getValue()).getDiscount();
        }

        double cost = STARTING_COST +  (double) (distance / METERS_IN_KILOMETER) * COST_OF_KILOMETER;
        return cost - (cost * discount);
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
