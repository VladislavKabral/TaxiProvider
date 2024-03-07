package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.repository.ride.AddressesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressesService {

    private final AddressesRepository addressesRepository;

    @Transactional(readOnly = true)
    public Address findByLatitudeAndLongitude(String latitude, String longitude) {
        return addressesRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    @Transactional
    public void save(Address address) {
        addressesRepository.save(address);
    }
}
