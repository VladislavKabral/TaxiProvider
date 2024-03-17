package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {

    private static final String DATE_FORMAT = "yyyy-MM-dd, HH-mm-ss";

    private long id;

    @Min(value = 1, message = PASSENGER_ID_IS_INVALID)
    private long passengerId;

    @Min(value = 1, message = DRIVER_ID_IS_INVALID)
    private long driverId;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime startedAt;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime endedAt;

    @Valid
    private AddressDTO sourceAddress;

    @Valid
    @Size(min = 1, message = DESTINATION_ADDRESSES_COUNT_IS_INVALID)
    private List<AddressDTO> destinationAddresses;

    @Valid
    private PromoCodeDTO promoCode;

    private BigDecimal cost;

    private String status;
}
