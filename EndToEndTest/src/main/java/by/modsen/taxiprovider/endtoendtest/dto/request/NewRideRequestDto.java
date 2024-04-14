package by.modsen.taxiprovider.endtoendtest.dto.request;

import by.modsen.taxiprovider.endtoendtest.dto.ride.AddressDto;
import by.modsen.taxiprovider.endtoendtest.dto.promocode.PromoCodeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewRideRequestDto {

    private long passengerId;

    private AddressDto sourceAddress;

    private List<AddressDto> destinationAddresses;

    private PromoCodeDto promoCode;

    private String paymentType;
}
