package by.modsen.taxiprovider.ridesservice.repository.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressesRepository extends JpaRepository<Address, Long> {
    Address findByLatAndLon(String lat, String lon);
}
