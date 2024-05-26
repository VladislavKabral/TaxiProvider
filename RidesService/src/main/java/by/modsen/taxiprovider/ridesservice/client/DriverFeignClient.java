package by.modsen.taxiprovider.ridesservice.client;

import by.modsen.taxiprovider.ridesservice.config.FeignClientConfig;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.DriverResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${feign.client.config.driver.name}",
        configuration = FeignClientConfig.class,
        path = "${feign.client.config.driver.path}")
public interface DriverFeignClient {

    @GetMapping("/free")
    ResponseEntity<DriverListDto> getFreeDrivers();

    @GetMapping("/{id}")
    ResponseEntity<DriverDto> getDriverById(@PathVariable("id") long id);

    @PutMapping("/{id}")
    ResponseEntity<DriverResponseDto> editDriver(@PathVariable("id") long id, @RequestBody DriverDto driverDTO);
}
