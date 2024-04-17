package by.modsen.taxiprovider.endtoendtest.exception.handler;

import by.modsen.taxiprovider.endtoendtest.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.endtoendtest.exception.FeignClientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionAPIHandler {

    @ExceptionHandler(value = {FeignClientException.class})
    public ResponseEntity<ErrorResponseDto> handleFeignClientException(FeignClientException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }
}
