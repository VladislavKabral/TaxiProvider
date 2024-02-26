package by.modsen.taxiprovider.ridesservice.repository.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.address.DestinationAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinationAddressesRepository extends JpaRepository<DestinationAddress, Long> {
}
