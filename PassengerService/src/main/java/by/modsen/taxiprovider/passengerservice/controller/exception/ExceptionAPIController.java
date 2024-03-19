package by.modsen.taxiprovider.passengerservice.controller.exception;

import by.modsen.taxiprovider.passengerservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.data.mapping.PropertyReferenceException;

import static by.modsen.taxiprovider.passengerservice.util.Message.*;

import java.net.ConnectException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionAPIController {

    @ExceptionHandler(value = {
            InvalidRequestDataException.class,
            EntityValidateException.class,
            HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class,
            PropertyReferenceException.class
    })
    public ResponseEntity<ErrorResponseDTO> defaultMessageExceptionHandler(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> entityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentTypeMismatchException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(REQUEST_PARAMETER_IS_INVALID)
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponseDTO.builder()
                        .message(String.format(METHOD_NOT_ALLOWED, exception.getMessage()))
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(ConnectException.class)
    public ResponseEntity<ErrorResponseDTO> connectException() {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.builder()
                        .message(EXTERNAL_SERVICE_IS_UNAVAILABLE)
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());

    }
}
