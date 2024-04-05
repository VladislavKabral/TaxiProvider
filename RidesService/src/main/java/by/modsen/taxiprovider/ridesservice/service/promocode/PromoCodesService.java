package by.modsen.taxiprovider.ridesservice.service.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDto;
import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodesListDto;
import by.modsen.taxiprovider.ridesservice.dto.response.PromoCodeResponseDto;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.ridesservice.util.validation.promocode.PromoCodeValidator;
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

    private final PromoCodeValidator promoCodeValidator;

    @Transactional(readOnly = true)
    public PromoCodesListDto findAll() {
        List<PromoCode> promoCodes = promoCodesRepository.findAll();

        return PromoCodesListDto.builder()
                .content(promoCodeMapper.toListDto(promoCodes))
                .build();
    }

    @Transactional(readOnly = true)
    public PromoCodeDto findByValue(String value) throws EntityNotFoundException {
        return promoCodeMapper.toDto(promoCodesRepository.findByValue(value).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, value))));
    }

    @Transactional
    public PromoCodeResponseDto save(PromoCodeDto promoCodeDTO)
            throws EntityValidateException, EntityNotFoundException {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCodeValidator.validate(promoCode);

        promoCodesRepository.save(promoCode);

        return new PromoCodeResponseDto(promoCodesRepository
                .findByValue(promoCode.getValue())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PROMO_CODE_NOT_CREATED, promoCode.getValue())))
                .getId());
    }

    @Transactional
    public PromoCodeResponseDto update(long id, PromoCodeDto promoCodeDTO) throws EntityValidateException {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCodeValidator.validate(promoCode);
        promoCode.setId(id);
        promoCodesRepository.save(promoCode);

        return new PromoCodeResponseDto(id);
    }

    @Transactional
    public PromoCodeResponseDto delete(long id) throws EntityNotFoundException {
        PromoCode promoCode = promoCodesRepository.findById(id)
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PROMO_CODE_WITH_ID_NOT_FOUND, id)));
        promoCodesRepository.delete(promoCode);
        return new PromoCodeResponseDto(id);
    }
}
