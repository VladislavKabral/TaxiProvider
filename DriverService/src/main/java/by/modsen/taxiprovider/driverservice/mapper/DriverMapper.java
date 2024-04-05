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

    public Driver toEntity(NewDriverDto newDriverDto) {
        return modelMapper.map(newDriverDto, Driver.class);
    }

    public DriverDto toDto(Driver driver) {
        return modelMapper.map(driver, DriverDto.class);
    }

    public List<DriverDto> toListDto(List<Driver> drivers) {
        return drivers.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
