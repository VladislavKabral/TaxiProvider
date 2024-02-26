package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.address.AddressDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.address.DestinationAddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {

    private long passengerId;

    private long driverId;

    @JsonFormat(pattern = "yyyy-MM-dd, HH-mm-ss")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd, HH-mm-ss")
    private LocalDateTime endedAt;

    private AddressDTO sourceAddress;

    private List<DestinationAddressDTO> destinationAddresses;

    private PromoCodeDTO promoCode;
}
