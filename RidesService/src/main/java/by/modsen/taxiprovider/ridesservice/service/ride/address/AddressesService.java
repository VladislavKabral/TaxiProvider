package by.modsen.taxiprovider.ridesservice.service.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import by.modsen.taxiprovider.ridesservice.repository.ride.address.AddressesRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class AddressesService {

    private final AddressesRepository addressesRepository;

    @Transactional
    public void save(Address address) {
        addressesRepository.save(address);
    }
}
