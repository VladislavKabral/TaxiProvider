package by.modsen.taxiprovider.endtoendtest.client;

import by.modsen.taxiprovider.endtoendtest.config.FeignConfig;
import by.modsen.taxiprovider.endtoendtest.dto.request.NewRideRequestDto;
import by.modsen.taxiprovider.endtoendtest.dto.response.RideResponseDto;
import by.modsen.taxiprovider.endtoendtest.dto.response.RidesListResponseDto;
import by.modsen.taxiprovider.endtoendtest.dto.ride.RideDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static by.modsen.taxiprovider.endtoendtest.util.EndToEntTestUtil.*;

@FeignClient(value = "${rides-service.name}", url = RIDES_SERVICE_PATH, configuration = FeignConfig.class)
public interface RidesServiceClient {

    @GetMapping(GET_PASSENGER_RIDES_PATH)
    ResponseEntity<RidesListResponseDto> getPassengerRides(@PathVariable("id") long id);

    @GetMapping(GET_DRIVER_RIDES_PATH)
    ResponseEntity<RidesListResponseDto> getDriverRides(@PathVariable("id") long id);

    @PostMapping(consumes = CONTENT_TYPE)
    ResponseEntity<RideResponseDto> saveRide(@RequestBody NewRideRequestDto rideDTO);

    @PatchMapping(consumes = CONTENT_TYPE)
    ResponseEntity<RideResponseDto> updateRide(@RequestBody RideDto rideDTO);

    @PatchMapping(value = GET_PASSENGER_RIDE_PATH, consumes = CONTENT_TYPE)
    ResponseEntity<RideResponseDto> cancelRide(@PathVariable("passengerId") long passengerId);
}
