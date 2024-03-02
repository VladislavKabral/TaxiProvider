package by.modsen.taxiprovider.ridesservice.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

    private String message;

    @JsonFormat(pattern = "yy-MM-dd, HH-mm-ss")
    private ZonedDateTime time;
}
