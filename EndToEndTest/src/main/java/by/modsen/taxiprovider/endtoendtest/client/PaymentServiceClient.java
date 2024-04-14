package by.modsen.taxiprovider.endtoendtest.client;

import by.modsen.taxiprovider.endtoendtest.config.FeignConfig;
import by.modsen.taxiprovider.endtoendtest.dto.payment.ChargeResponseDto;
import by.modsen.taxiprovider.endtoendtest.dto.request.CustomerChargeRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static by.modsen.taxiprovider.endtoendtest.util.EndToEntTestUtil.*;

@FeignClient(value = "${payment-service.name}", url = PAYMENT_SERVICE_PATH, configuration = FeignConfig.class)
public interface PaymentServiceClient {

    @PostMapping(value = PAYMENT_RIDE_PATH, consumes = CONTENT_TYPE)
    ResponseEntity<ChargeResponseDto> chargeByCustomer(@RequestBody CustomerChargeRequestDto customerChargeRequestDTO);
}
