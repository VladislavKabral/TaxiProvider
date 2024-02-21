package by.modsen.taxiprovider.passengerservice.service.card;

import by.modsen.taxiprovider.passengerservice.model.card.CreditCard;
import by.modsen.taxiprovider.passengerservice.repository.card.CreditCardsRepository;
import by.modsen.taxiprovider.passengerservice.util.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CreditCardService {

    private final CreditCardsRepository creditCardsRepository;

    @Autowired
    public CreditCardService(CreditCardsRepository creditCardsRepository) {
        this.creditCardsRepository = creditCardsRepository;
    }

    public CreditCard getByNumber(String number) throws EntityNotFoundException {
        Optional<CreditCard> creditCard = creditCardsRepository.findByNumber(number);

        return creditCard.orElseThrow(EntityNotFoundException.entityNotFoundException("Credit card with number '" + number +"' wasn't found"));
    }

    @Transactional
    public void save(CreditCard creditCard) {
        creditCardsRepository.save(creditCard);
    }
}
