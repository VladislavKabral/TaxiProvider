package by.modsen.taxiprovider.passengerservice.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestDTO {

    private long passengerId;

    @Valid
    @NotNull(message = "Source address must be not empty")
    private AddressDTO sourceAddress;

    @Valid
    @NotNull(message = "Destination address-(es) must be not empty")
    @Size(min = 1, message = "Must be at least one destination address")
    private List<AddressDTO> destinationAddresses;

    @Valid
    private PromoCodeDTO promoCode;
}
