package by.modsen.taxiprovider.endtoendtest.client;

import by.modsen.taxiprovider.endtoendtest.config.FeignConfig;
import by.modsen.taxiprovider.endtoendtest.dto.driver.DriverDto;
import by.modsen.taxiprovider.endtoendtest.dto.driver.DriverListDto;
import by.modsen.taxiprovider.endtoendtest.dto.driver.DriverProfileDto;
import by.modsen.taxiprovider.endtoendtest.dto.driver.DriverResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import static by.modsen.taxiprovider.endtoendtest.util.EndToEntTestUtil.*;

@FeignClient(value = "${driver-service.name}", url = DRIVERS_SERVICE_PATH, configuration = FeignConfig.class)
public interface DriverServiceClient {

    @GetMapping(GET_DRIVER_PATH)
    ResponseEntity<DriverDto> getDriverById(@PathVariable("id") long id);

    @GetMapping(GET_PROFILE_PATH)
    ResponseEntity<DriverProfileDto> getDriverProfile(@PathVariable("id") long id);

    @GetMapping(GET_FREE_DRIVERS_PATH)
    ResponseEntity<DriverListDto> getFreeDrivers();

    @PatchMapping(value = GET_DRIVER_PATH, consumes = CONTENT_TYPE)
    ResponseEntity<DriverResponseDto> editDriver(@PathVariable("id") long id, @RequestBody DriverDto driverDTO);
}
