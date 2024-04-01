package by.modsen.taxiprovider.driverservice.mapper;

import by.modsen.taxiprovider.driverservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.driverservice.dto.driver.NewDriverDto;
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

    public Driver toEntity(NewDriverDto newDriverDTO) {
        return modelMapper.map(newDriverDTO, Driver.class);
    }

    public DriverDto toDTO(Driver driver) {
        return modelMapper.map(driver, DriverDto.class);
    }

    public List<DriverDto> toListDTO(List<Driver> drivers) {
        return drivers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
