package by.modsen.taxiprovider.endtoendtest.client;

import by.modsen.taxiprovider.endtoendtest.config.FeignConfig;
import by.modsen.taxiprovider.endtoendtest.dto.passenger.PassengerProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import static by.modsen.taxiprovider.endtoendtest.util.EndToEntTestUtil.*;

@FeignClient(name = "${passenger-service.name}", url = PASSENGERS_SERVICE_PATH, configuration = FeignConfig.class)
public interface PassengerServiceClient {

    @GetMapping(GET_PROFILE_PATH)
    ResponseEntity<PassengerProfileDto> getPassengerProfile(@PathVariable long id);
}
