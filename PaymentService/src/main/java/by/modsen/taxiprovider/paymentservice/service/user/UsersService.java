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

    public User findById(long id) throws EntityNotFoundException {
        return usersRepository.findById(id).orElseThrow(EntityNotFoundException
                .entityNotFoundException("User with id '" + id + "' wasn't found"));
    }

    @Transactional
    public void save(User user) {
        usersRepository.save(user);
    }

    @Transactional
    public void delete(long id) throws EntityNotFoundException {
        User user = findById(id);
        usersRepository.delete(user);
    }

}
