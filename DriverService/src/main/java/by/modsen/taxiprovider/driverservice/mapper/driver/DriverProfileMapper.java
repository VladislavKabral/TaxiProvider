package by.modsen.taxiprovider.driverservice.mapper.driver;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverProfileDTO;
import by.modsen.taxiprovider.driverservice.model.driver.DriverProfile;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DriverProfileMapper {

    private final ModelMapper modelMapper;

    public DriverProfileDTO toDTO(DriverProfile driverProfile) {
        return modelMapper.map(driverProfile, DriverProfileDTO.class);
    }
}
