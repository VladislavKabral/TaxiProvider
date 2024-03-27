package by.modsen.taxiprovider.ratingservice.controller.exception;

import by.modsen.taxiprovider.ratingservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ratingservice.util.exception.EntityValidateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ExceptionAPIController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> entityNotFoundException(EntityNotFoundException exception) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .message(exception.getMessage())
                .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            EntityValidateException.class,
            HttpMessageNotReadableException.class,
            UnsatisfiedServletRequestParameterException.class
    })
    public ResponseEntity<ErrorResponseDTO> defaultMessageExceptionHandler(Exception exception) {
        return new ResponseEntity<>(ErrorResponseDTO.builder()
                .message(exception.getMessage())
                .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> methodArgumentTypeMismatchException() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message("Failed to convert value in request parameter")
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage() + " for this endpoint")
                        .time(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build());
    }
}
