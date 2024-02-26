package by.modsen.taxiprovider.ridesservice.mapper.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
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

    public PromoCode toEntity(PromoCodeDTO promoCodeDTO) {
        return modelMapper.map(promoCodeDTO, PromoCode.class);
    }

    public PromoCodeDTO toDTO(PromoCode promoCode) {
        return modelMapper.map(promoCode, PromoCodeDTO.class);
    }

    public List<PromoCodeDTO> toListDTO(List<PromoCode> promoCodes) {
        return promoCodes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
