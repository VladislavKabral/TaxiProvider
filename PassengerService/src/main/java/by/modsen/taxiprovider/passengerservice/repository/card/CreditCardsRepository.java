package by.modsen.taxiprovider.passengerservice.repository.card;

import by.modsen.taxiprovider.passengerservice.model.card.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditCardsRepository extends JpaRepository<CreditCard, Long> {
}
