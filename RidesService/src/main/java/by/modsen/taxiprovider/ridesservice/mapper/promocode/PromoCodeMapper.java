package by.modsen.taxiprovider.ridesservice.mapper.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromoCodeMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public PromoCodeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

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
