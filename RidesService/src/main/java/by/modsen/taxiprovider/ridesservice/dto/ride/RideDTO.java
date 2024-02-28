package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.address.AddressDTO;
import by.modsen.taxiprovider.ridesservice.dto.ride.address.DestinationAddressDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDTO {

    @Min(value = 1, message = "Passenger's id must be a number and can't be less than one")
    private long passengerId;

    @Min(value = 1, message = "Driver's id must be a number and can't be less than one")
    private long driverId;

    @JsonFormat(pattern = "yyyy-MM-dd, HH-mm-ss")
    private LocalDateTime startedAt;

    @JsonFormat(pattern = "yyyy-MM-dd, HH-mm-ss")
    private LocalDateTime endedAt;

    @Valid
    @NotNull(message = "Source address must be not empty")
    private AddressDTO sourceAddress;

    @Valid
    @NotNull(message = "Target address-(es) must be not empty")
    @Size(min = 1, message = "Must be at least one destination address")
    private List<DestinationAddressDTO> destinationAddresses;

    @Valid
    private PromoCodeDTO promoCode;

    private double cost;
}
