package by.modsen.taxiprovider.paymentservice.util.validation;

import by.modsen.taxiprovider.paymentservice.dto.CustomerDto;
import by.modsen.taxiprovider.paymentservice.service.user.UsersService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final UsersService usersService;

    public void validate(CustomerDto customerDTO) throws EntityValidateException {
        if (usersService.isUserExists(customerDTO.getTaxiUserId(), customerDTO.getRole())) {
            throw new EntityValidateException(String.format(TAXI_USER_ALREADY_EXISTS, customerDTO.getTaxiUserId(),
                    customerDTO.getRole()));
        }
    }
}
