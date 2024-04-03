package by.modsen.taxiprovider.ridesservice.dto.ride;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewRideDTO {

    @Min(value = 1, message = PASSENGER_ID_MINIMAL_VALUE_IS_INVALID)
    private long passengerId;

    @Valid
    @NotNull(message = SOURCE_ADDRESS_IS_EMPTY)
    private AddressDTO sourceAddress;

    @Valid
    @NotNull(message = DESTINATION_ADDRESS_IS_EMPTY)
    @Size(min = 1, message = DESTINATION_ADDRESSES_COUNT_IS_INVALID)
    private List<AddressDTO> destinationAddresses;

    @Valid
    private PromoCodeDTO promoCode;

    @NotBlank(message = PAYMENT_TYPE_IS_EMPTY)
    private String paymentType;
}
