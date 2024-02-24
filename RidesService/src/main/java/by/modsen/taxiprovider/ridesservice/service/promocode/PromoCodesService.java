package by.modsen.taxiprovider.ridesservice.service.promocode;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import by.modsen.taxiprovider.ridesservice.repository.promocode.PromoCodesRepository;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PromoCodesService {

    private final PromoCodesRepository promoCodesRepository;

    @Autowired
    public PromoCodesService(PromoCodesRepository promoCodesRepository) {
        this.promoCodesRepository = promoCodesRepository;
    }

    public List<PromoCode> findAll() throws EntityNotFoundException {
        List<PromoCode> promoCodes = promoCodesRepository.findAll();

        if (promoCodes.isEmpty()) {
            throw new EntityNotFoundException("There aren't any promo codes");
        }

        return promoCodes;
    }

    public PromoCode findById(long id) throws EntityNotFoundException {
        Optional<PromoCode> promoCode = promoCodesRepository.findById(id);

        return promoCode.orElseThrow(EntityNotFoundException
                .entityNotFoundException("Promo code with id '" + id + "' wasn't found"));
    }

    public PromoCode findByValue(String value) throws EntityNotFoundException {
        Optional<PromoCode> promoCode = promoCodesRepository.findByValue(value);

        return promoCode.orElseThrow(EntityNotFoundException
                .entityNotFoundException("Promo code '" + value + "' wasn't found"));
    }

    @Transactional
    public void save(PromoCode promoCode) {
        promoCodesRepository.save(promoCode);
    }

    @Transactional
    public void update(long id, PromoCode promoCode) {
        promoCode.setId(id);
        promoCodesRepository.save(promoCode);
    }

    @Transactional
    public void delete(long id) throws EntityNotFoundException {
        PromoCode promoCode = findById(id);

        promoCodesRepository.delete(promoCode);
    }
}
