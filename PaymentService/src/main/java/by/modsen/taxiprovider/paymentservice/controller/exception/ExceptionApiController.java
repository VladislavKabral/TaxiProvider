package by.modsen.taxiprovider.paymentservice.controller.exception;

import by.modsen.taxiprovider.paymentservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionApiController {

    @ExceptionHandler(value = {
            NotEnoughMoneyException.class,
            EntityNotFoundException.class,
            PaymentException.class
    })
    public ResponseEntity<ErrorResponseDTO> exceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }
}
