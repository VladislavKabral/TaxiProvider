package by.modsen.taxiprovider.ridesservice.service.promocode;

import by.modsen.taxiprovider.ridesservice.dto.promocode.PromoCodeDTO;
import by.modsen.taxiprovider.ridesservice.mapper.promocode.PromoCodeMapper;
import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PromoCodesService {

    private final PromoCodesRepository promoCodesRepository;

    private final PromoCodeMapper promoCodeMapper;

    @Transactional(readOnly = true)
    public List<PromoCodeDTO> findAll() throws EntityNotFoundException {
        List<PromoCode> promoCodes = promoCodesRepository.findAll();

        if (promoCodes.isEmpty()) {
            throw new EntityNotFoundException("There aren't any promo codes");
        }

        return promoCodeMapper.toListDTO(promoCodes);
    }

    @Transactional(readOnly = true)
    public PromoCodeDTO findByValue(String value) throws EntityNotFoundException {
        return promoCodeMapper.toDTO(promoCodesRepository.findByValue(value).orElseThrow(EntityNotFoundException
                .entityNotFoundException("Promo code '" + value + "' wasn't found")));
    }

    @Transactional
    public void save(PromoCodeDTO promoCodeDTO) {
        promoCodesRepository.save(promoCodeMapper.toEntity(promoCodeDTO));
    }

    @Transactional
    public void update(long id, PromoCodeDTO promoCodeDTO) {
        PromoCode promoCode = promoCodeMapper.toEntity(promoCodeDTO);
        promoCode.setId(id);
        promoCodesRepository.save(promoCode);
    }

    @Transactional
    public void delete(long id) throws EntityNotFoundException {
        PromoCode promoCode = promoCodesRepository.findById(id)
                .orElseThrow(EntityNotFoundException.entityNotFoundException("Promo code with id '" + id + "' wasn't found"));

        promoCodesRepository.delete(promoCode);
    }
}
