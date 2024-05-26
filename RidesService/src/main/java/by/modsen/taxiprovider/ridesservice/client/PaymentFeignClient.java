package by.modsen.taxiprovider.ridesservice.client;

import by.modsen.taxiprovider.ridesservice.config.FeignClientConfig;
import by.modsen.taxiprovider.ridesservice.dto.request.CustomerChargeRequestDto;
import by.modsen.taxiprovider.ridesservice.dto.response.ChargeResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${feign.client.config.payment.name}",
        configuration = FeignClientConfig.class,
        path = "${feign.client.config.payment.path}")
public interface PaymentFeignClient {

    @PostMapping("/customerCharge")
    ResponseEntity<ChargeResponseDto> chargeByCustomer(@RequestBody @Valid CustomerChargeRequestDto customerChargeRequestDTO);
}
