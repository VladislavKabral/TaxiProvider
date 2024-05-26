package by.modsen.taxiprovider.driverservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxiUserRequestDto {

    private long taxiUserId;

    private String role;
}
