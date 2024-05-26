package by.modsen.taxiprovider.paymentservice.client;

import by.modsen.taxiprovider.paymentservice.config.FeignClientConfig;
import by.modsen.taxiprovider.paymentservice.dto.request.RideDto;
import by.modsen.taxiprovider.paymentservice.dto.response.RideResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${feign.client.config.ride.name}",
        configuration = FeignClientConfig.class,
        path = "${feign.client.config.ride.path}")
public interface RideFeignClient {

    @PutMapping
    ResponseEntity<RideResponseDto> updateRide(@RequestBody RideDto rideDTO);
}
