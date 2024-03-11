package by.modsen.taxiprovider.paymentservice.service.user;

import by.modsen.taxiprovider.paymentservice.model.User;
import by.modsen.taxiprovider.paymentservice.repository.UsersRepository;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public Boolean isUserExists(long taxiUserId, String role) {
        return usersRepository.findByTaxiUserIdAndRole(taxiUserId, role).isPresent();
    }

    public User findByTaxiUserIdAndRole(long id, String role) throws EntityNotFoundException {
        return usersRepository.findByTaxiUserIdAndRole(id, role).orElseThrow(EntityNotFoundException
                .entityNotFoundException("User with id '" + id + "' wasn't found"));
    }

    @Transactional
    public void save(User user) {
        usersRepository.save(user);
    }

    @Transactional
    public void delete(long id, String role) throws EntityNotFoundException {
        User user = findByTaxiUserIdAndRole(id, role);
        usersRepository.delete(user);
    }

}
