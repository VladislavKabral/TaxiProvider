package by.modsen.taxiprovider.apigateway.controller;

import by.modsen.taxiprovider.apigateway.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static by.modsen.taxiprovider.apigateway.util.Message.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/driver-service")
    public ResponseEntity<ErrorResponseDto> driverServiceFallback() {
        return new ResponseEntity<>(ErrorResponseDto.buildErrorResponse(DRIVER_SERVICE_IS_NOT_AVAILABLE),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/passenger-service")
    public ResponseEntity<ErrorResponseDto> passengerServiceFallback() {
        return new ResponseEntity<>(ErrorResponseDto.buildErrorResponse(PASSENGER_SERVICE_IS_NOT_AVAILABLE),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/rating-service")
    public ResponseEntity<ErrorResponseDto> ratingServiceFallback() {
        return new ResponseEntity<>(ErrorResponseDto.buildErrorResponse(RATING_SERVICE_IS_NOT_AVAILABLE),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/payment-service")
    public ResponseEntity<ErrorResponseDto> paymentServiceFallback() {
        return new ResponseEntity<>(ErrorResponseDto.buildErrorResponse(PAYMENT_SERVICE_IS_NOT_AVAILABLE),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @GetMapping("/ride-service")
    public ResponseEntity<ErrorResponseDto> rideServiceFallback() {
        return new ResponseEntity<>(ErrorResponseDto.buildErrorResponse(RIDE_SERVICE_IS_NOT_AVAILABLE),
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}
