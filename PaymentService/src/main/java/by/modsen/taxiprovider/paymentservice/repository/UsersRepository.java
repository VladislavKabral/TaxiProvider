package by.modsen.taxiprovider.paymentservice.repository;

import by.modsen.taxiprovider.paymentservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByTaxiUserIdAndRole(long id, String role);
}
