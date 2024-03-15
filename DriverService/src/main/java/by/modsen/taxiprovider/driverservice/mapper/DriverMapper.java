package by.modsen.taxiprovider.driverservice.mapper;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDTO;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDTO;
import by.modsen.taxiprovider.driverservice.model.Driver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DriverMapper {

    private final ModelMapper modelMapper;

    public Driver toEntity(NewDriverDTO newDriverDTO) {
        return modelMapper.map(newDriverDTO, Driver.class);
    }

    public DriverDTO toDTO(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }

    public List<DriverDTO> toListDTO(List<Driver> drivers) {
        return drivers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
