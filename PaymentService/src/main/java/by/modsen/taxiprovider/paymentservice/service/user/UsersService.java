package by.modsen.taxiprovider.paymentservice.service.user;

import by.modsen.taxiprovider.paymentservice.model.User;
import by.modsen.taxiprovider.paymentservice.repository.UsersRepository;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository usersRepository;

    public Boolean isUserExists(long taxiUserId, String role) {
        return usersRepository.findByTaxiUserIdAndRole(taxiUserId, role).isPresent();
    }

    public User findByTaxiUserIdAndRole(long id, String role) throws EntityNotFoundException {
        return usersRepository.findByTaxiUserIdAndRole(id, role).orElseThrow(EntityNotFoundException
                .entityNotFoundException(String.format(USER_NOT_FOUND, id, role)));
    }

    @Transactional
    public void save(User user) {
        log.info(SAVING_NEW_USER);
        usersRepository.save(user);
        log.info(String.format(NEW_USER_WAS_SAVED, user.getTaxiUserId(), user.getRole()));
    }

    @Transactional
    public void delete(long id, String role) throws EntityNotFoundException {
        log.info(String.format(DELETING_USER, id, role));
        User user = findByTaxiUserIdAndRole(id, role);
        usersRepository.delete(user);
        log.info(String.format(USER_WAS_DELETED, user.getTaxiUserId(), user.getRole()));
    }

}
