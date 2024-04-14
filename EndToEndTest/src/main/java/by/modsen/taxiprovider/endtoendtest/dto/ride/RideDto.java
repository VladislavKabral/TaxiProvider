package by.modsen.taxiprovider.endtoendtest.dto.ride;

import by.modsen.taxiprovider.endtoendtest.dto.promocode.PromoCodeDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideDto {

    private static final String DATE_FORMAT = "yyyy-MM-dd, HH-mm-ss";

    private long id;

    private long passengerId;

    private long driverId;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime startedAt;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime endedAt;

    private AddressDto sourceAddress;

    private List<AddressDto> destinationAddresses;

    private PromoCodeDto promoCode;

    private BigDecimal cost;

    private String status;

    private String paymentType;
}
