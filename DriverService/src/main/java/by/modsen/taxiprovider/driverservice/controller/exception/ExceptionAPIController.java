package by.modsen.taxiprovider.driverservice.controller.exception;

import by.modsen.taxiprovider.driverservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.driverservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.driverservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.driverservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.driverservice.util.exception.ExternalServiceUnavailableException;
import by.modsen.taxiprovider.driverservice.util.exception.InvalidRequestDataException;
import java.net.ConnectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.data.mapping.PropertyReferenceException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static by.modsen.taxiprovider.driverservice.util.Message.*;

@RestControllerAdvice
public class ExceptionAPIController {

    @ExceptionHandler(value = {
            ExternalServiceRequestException.class,
            ExternalServiceUnavailableException.class,
            InvalidRequestDataException.class,
            EntityValidateException.class,
            HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class,
            PropertyReferenceException.class
    })
    public ResponseEntity<ErrorResponseDto> defaultMessageExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> entityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        StringBuilder errorMessage = new StringBuilder();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> errorMessage.append(error.getDefaultMessage()).append(" "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(errorMessage.toString().trim())
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

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponseDto> connectException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder()
                        .message(EXTERNAL_SERVICE_IS_UNAVAILABLE)
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());

    }
}
