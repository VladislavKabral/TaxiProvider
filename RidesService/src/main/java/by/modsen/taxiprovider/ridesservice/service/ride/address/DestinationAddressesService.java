package by.modsen.taxiprovider.ridesservice.service.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.address.DestinationAddress;
import by.modsen.taxiprovider.ridesservice.repository.ride.address.DestinationAddressesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class DestinationAddressesService {

    private final DestinationAddressesRepository destinationAddressesRepository;

    @Transactional
    public void save(List<DestinationAddress> destinationAddresses) {
        destinationAddressesRepository.saveAll(destinationAddresses);
    }
}
