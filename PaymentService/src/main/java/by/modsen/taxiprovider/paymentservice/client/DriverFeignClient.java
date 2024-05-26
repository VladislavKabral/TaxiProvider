package by.modsen.taxiprovider.paymentservice.client;

import by.modsen.taxiprovider.paymentservice.config.FeignClientConfig;
import by.modsen.taxiprovider.paymentservice.dto.response.DriverDto;
import by.modsen.taxiprovider.paymentservice.dto.response.DriverResponseDto;
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

    @GetMapping("/{id}")
    ResponseEntity<DriverDto> getDriverById(@PathVariable("id") long id);

    @PutMapping("/{id}")
    ResponseEntity<DriverResponseDto> editDriver(@PathVariable("id") long id, @RequestBody DriverDto driverDTO);
}
