package by.modsen.taxiprovider.ridesservice.mapper.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.AddressDto;
import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AddressMapper {

    private AddressDto toDTO(Address address) {
        return AddressDto.builder()
                .latitude(Double.valueOf(address.getLatitude()))
                .longitude(Double.valueOf(address.getLongitude()))
                .build();
    }

    public List<AddressDto> toListDTO(List<Address> addresses) {
        return addresses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
