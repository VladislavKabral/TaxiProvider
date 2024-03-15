package by.modsen.taxiprovider.ratingservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxiUserRequestDTO {

    @NotNull(message = "Taxi user's id must be not empty")
    @Min(value = 1, message = "Minimal value for taxi user's id is 1")
    private long taxiUserId;

    @NotBlank(message = "Taxi user's role must be not empty")
    private String role;
}
