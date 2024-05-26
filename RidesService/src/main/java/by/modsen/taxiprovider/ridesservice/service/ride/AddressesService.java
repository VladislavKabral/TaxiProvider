package by.modsen.taxiprovider.ridesservice.service.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.repository.ride.AddressesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressesService {

    private final AddressesRepository addressesRepository;

    @Transactional(readOnly = true)
    public Address findByLatitudeAndLongitude(String latitude, String longitude) {
        log.info(FINDING_ADDRESS);
        return addressesRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    @Transactional
    public void save(Address address) {
        log.info(SAVING_NEW_ADDRESS);
        addressesRepository.save(address);
        log.info(NEW_ADDRESS_WAS_SAVED);
    }
}
