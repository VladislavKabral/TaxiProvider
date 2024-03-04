package by.modsen.taxiprovider.paymentservice.mapper;

import by.modsen.taxiprovider.paymentservice.dto.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.model.ChargeRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChargeRequestMapper {

    private final ModelMapper modelMapper;

    public ChargeRequest toEntity(ChargeRequestDTO chargeRequestDTO) {
        return modelMapper.map(chargeRequestDTO, ChargeRequest.class);
    }
}
