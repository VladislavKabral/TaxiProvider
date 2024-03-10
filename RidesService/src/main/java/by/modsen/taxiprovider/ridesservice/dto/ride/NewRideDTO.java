package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRideDTO {

    @Min(value = 1, message = "Passenger's id must be a number and can't be less than one")
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
