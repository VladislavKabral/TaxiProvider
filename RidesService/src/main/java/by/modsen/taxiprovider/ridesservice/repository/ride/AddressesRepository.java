package by.modsen.taxiprovider.ridesservice.repository.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesRepository extends JpaRepository<Address, Long> {

    Address findByLatitudeAndLongitude(String latitude, String longitude);
}
