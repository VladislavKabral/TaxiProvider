package by.modsen.taxiprovider.driverservice.client;

import by.modsen.taxiprovider.driverservice.config.FeignClientConfig;
import by.modsen.taxiprovider.driverservice.dto.rating.TaxiUserRatingDto;
import by.modsen.taxiprovider.driverservice.dto.request.TaxiUserRequestDto;
import by.modsen.taxiprovider.driverservice.dto.response.RatingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${feign.client.config.rating.name}",
        configuration = FeignClientConfig.class,
        path = "${feign.client.config.rating.path}")
public interface RatingFeignClient {

    @PostMapping("/init")
    ResponseEntity<RatingResponseDto> initTaxiUser(@RequestBody TaxiUserRequestDto requestDTO);

    @GetMapping(params = {"taxiUserId", "role"})
    ResponseEntity<TaxiUserRatingDto> getTaxiUserRating(@RequestParam("taxiUserId")  long taxiUserId,
                                                        @RequestParam("role") String role);
}
