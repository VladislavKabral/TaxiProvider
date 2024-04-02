package by.modsen.taxiprovider.ridesservice.service.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDTO;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Service
@RequiredArgsConstructor
public class PromoCodesService {

    private final PromoCodesRepository promoCodesRepository;

    private final PromoCodeMapper promoCodeMapper;

    @Transactional(readOnly = true)
    public List<PromoCodeDTO> findAll() throws EntityNotFoundException {
        List<PromoCode> promoCodes = promoCodesRepository.findAll();

        if (promoCodes.isEmpty()) {
            throw new EntityNotFoundException(PROMO_CODES_NOT_FOUND);
        }

        return promoCodeMapper.toListDTO(promoCodes);
    }

    @Transactional(readOnly = true)
    public PromoCodeDTO findByValue(String value) throws EntityNotFoundException {
        return promoCodeMapper.toDTO(promoCodesRepository.findByValue(value).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, value))));
    }

    @Transactional
    public PromoCodeResponseDTO save(PromoCodeDTO promoCodeDTO)
            throws EntityNotFoundException {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCodesRepository.save(promoCode);

        return new PromoCodeResponseDTO(promoCodesRepository
                .findByValue(promoCode.getValue())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PROMO_CODE_NOT_CREATED, promoCode.getValue())))
                .getId());
    }

    @Transactional
    public PromoCodeResponseDTO update(long id, PromoCodeDTO promoCodeDTO) {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCode.setId(id);
        promoCodesRepository.save(promoCode);

        return new PromoCodeResponseDTO(id);
    }

    @Transactional
    public PromoCodeResponseDTO delete(long id) throws EntityNotFoundException {
        PromoCode promoCode = promoCodesRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PROMO_CODE_WITH_ID_NOT_FOUND, id)));
        promoCodesRepository.delete(promoCode);
        return new PromoCodeResponseDTO(id);
    }
}
