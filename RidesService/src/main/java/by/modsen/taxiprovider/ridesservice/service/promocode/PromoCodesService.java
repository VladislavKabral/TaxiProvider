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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Service
@RequiredArgsConstructor
public class PromoCodesService {

    private final PromoCodesRepository promoCodesRepository;

    private final PromoCodeMapper promoCodeMapper;

    private final PromoCodeValidator promoCodeValidator;

    @Transactional(readOnly = true)
    public PromoCodesListDto findAll() throws EntityNotFoundException {
        List<PromoCode> promoCodes = promoCodesRepository.findAll();

        if (promoCodes.isEmpty()) {
            throw new EntityNotFoundException(PROMO_CODES_NOT_FOUND);
        }

        return PromoCodesListDto.builder()
                .content(promoCodeMapper.toListDTO(promoCodes))
                .build();
    }

    @Transactional(readOnly = true)
    public PromoCodeDto findByValue(String value) throws EntityNotFoundException {
        return promoCodeMapper.toDTO(promoCodesRepository.findByValue(value).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(PROMO_CODE_WITH_VALUE_NOT_FOUND, value))));
    }

    @Transactional
    public PromoCodeResponseDto save(PromoCodeDto promoCodeDTO, BindingResult bindingResult)
            throws EntityValidateException, EntityNotFoundException {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCodeValidator.validate(promoCode, bindingResult);
        handleBindingResult(bindingResult);

        promoCodesRepository.save(promoCode);

        return new PromoCodeResponseDto(promoCodesRepository
                .findByValue(promoCode.getValue())
                .orElseThrow(EntityNotFoundException
                        .entityNotFoundException(String.format(PROMO_CODE_NOT_CREATED, promoCode.getValue())))
                .getId());
    }

    @Transactional
    public PromoCodeResponseDto update(long id, PromoCodeDto promoCodeDTO, BindingResult bindingResult) throws EntityValidateException {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCodeValidator.validate(promoCode, bindingResult);
        handleBindingResult(bindingResult);
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

    private void handleBindingResult(BindingResult bindingResult) throws EntityValidateException {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();

            for (FieldError error: bindingResult.getFieldErrors()) {
                message.append(error.getDefaultMessage()).append(". ");
            }

            throw new EntityValidateException(message.toString());
        }
    }
}
