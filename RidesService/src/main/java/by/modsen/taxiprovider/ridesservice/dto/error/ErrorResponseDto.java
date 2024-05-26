package by.modsen.taxiprovider.ridesservice.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDto {

    private String message;

    @JsonFormat(pattern = "yy-MM-dd, HH-mm-ss")
    private LocalDateTime time;
}
