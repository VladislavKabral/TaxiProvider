package by.modsen.taxiprovider.ridesservice.mapper.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PromoCodeMapper {

    private final ModelMapper modelMapper;

    public PromoCode toEntity(PromoCodeDto promoCodeDTO) {
        return modelMapper.map(promoCodeDTO, PromoCode.class);
    }

    public PromoCodeDto toDTO(PromoCode promoCode) {
        return modelMapper.map(promoCode, PromoCodeDto.class);
    }

    public List<PromoCodeDto> toListDTO(List<PromoCode> promoCodes) {
        return promoCodes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
