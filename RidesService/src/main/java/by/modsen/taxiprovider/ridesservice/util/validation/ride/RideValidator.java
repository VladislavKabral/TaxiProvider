package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.RideDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Status.*;
import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.util.PaymentType.*;

@Component
@AllArgsConstructor
public class RideValidator implements Validator {

    private final List<String> rideStatuses = getRideStatuses();

    @Override
    public boolean supports(Class<?> clazz) {
        return RideDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RideDto ride = (RideDto) target;

        String status = ride.getStatus();
        if (!rideStatuses.contains(status)) {
            errors.rejectValue("status", "", RIDE_STATUS_IS_INVALID);
        }

        if (ride.getPaymentType() != null) {
            String paymentType = ride.getPaymentType();
            if ((!paymentType.equals(PAYMENT_TYPE_CASH)) && (!paymentType.equals(PAYMENT_TYPE_CARD))) {
                errors.rejectValue("paymentType", "", PAYMENT_TYPE_IS_INVALID);
            }
        }
    }
}
