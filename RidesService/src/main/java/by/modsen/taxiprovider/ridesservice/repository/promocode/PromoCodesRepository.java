package by.modsen.taxiprovider.ridesservice.repository.promocode;

import by.modsen.taxiprovider.ridesservice.model.promocode.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodesRepository extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByValue(String value);
}
