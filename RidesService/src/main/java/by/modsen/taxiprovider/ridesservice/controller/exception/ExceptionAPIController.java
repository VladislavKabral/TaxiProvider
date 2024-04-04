package by.modsen.taxiprovider.ridesservice.controller.exception;

import by.modsen.taxiprovider.ridesservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceUnavailableException;
import by.modsen.taxiprovider.ridesservice.util.exception.InvalidRequestDataException;

import java.net.ConnectException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import by.modsen.taxiprovider.ridesservice.util.exception.NotEnoughFreeDriversException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@RestControllerAdvice
public class ExceptionAPIController {

    @ExceptionHandler(value = {EntityNotFoundException.class,
            ExternalServiceRequestException.class,
            ExternalServiceUnavailableException.class,
            InvalidRequestDataException.class,
            EntityValidateException.class,
            DistanceCalculationException.class,
            HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponseDto> defaultMessageExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentTypeMismatchException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(REQUEST_PARAMETER_IS_INVALID)
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponseDto.builder()
                        .message(String.format(METHOD_NOT_ALLOWED, exception.getMessage()))
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponseDto> dateTimeParseException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(DATE_FORMAT_IS_INVALID)
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(NotEnoughFreeDriversException.class)
    public ResponseEntity<ErrorResponseDto> notEnoughFreeDriversException() {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .message(NO_FREE_DRIVERS)
                        .time(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponseDto> connectException(ConnectException connectException) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder()
                        .message(String.format(EXTERNAL_SERVICE_IS_UNAVAILABLE, connectException.getMessage()))
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());

    }
}
