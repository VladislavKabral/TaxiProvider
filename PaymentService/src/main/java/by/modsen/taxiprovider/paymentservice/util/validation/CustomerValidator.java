package by.modsen.taxiprovider.paymentservice.util.validation;

import by.modsen.taxiprovider.paymentservice.dto.CustomerDto;
import by.modsen.taxiprovider.paymentservice.service.user.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CustomerValidator implements Validator {

    private final UsersService usersService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CustomerDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerDto customerDTO = (CustomerDto) target;

        if (usersService.isUserExists(customerDTO.getTaxiUserId(), customerDTO.getRole())) {
            errors.rejectValue("taxiUserId", "",
                    "Taxi user with id '" + customerDTO.getTaxiUserId() +
                            "'and role '" + customerDTO.getRole() + "' already exists");
        }
    }
}
