package by.modsen.taxiprovider.passengerservice.controller.exception;

import by.modsen.taxiprovider.passengerservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.passengerservice.util.exception.InvalidRequestDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionAPIController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> entityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .time(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(InvalidRequestDataException.class)
    public ResponseEntity<ErrorResponseDTO> invalidRequestDataException(InvalidRequestDataException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .time(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(EntityValidateException.class)
    public ResponseEntity<ErrorResponseDTO> entityValidateException(EntityValidateException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.builder()
                        .message(exception.getMessage())
                        .time(LocalDateTime.now())
                        .build());
    }
}