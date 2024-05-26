package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideDto {

    private long id;

    @Min(value = 1, message = PASSENGER_ID_IS_INVALID)
    private long passengerId;

    @Min(value = 1, message = DRIVER_ID_IS_INVALID)
    private long driverId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endedAt;

    @Valid
    private AddressDto sourceAddress;

    @Valid
    @Size(min = 1, message = DESTINATION_ADDRESSES_COUNT_IS_INVALID)
    private List<AddressDto> destinationAddresses;

    @Valid
    private PromoCodeDto promoCode;

    private BigDecimal cost;

    private String status;

    private String paymentType;
}
