package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.dto.ride.RideDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static by.modsen.taxiprovider.ridesservice.util.Status.*;
import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.util.PaymentType.*;

@Component
@AllArgsConstructor
public class RideValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return RideDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RideDTO ride = (RideDTO) target;

        String status = ride.getStatus();
        if ((!status.equals(RIDE_STATUS_IN_PROGRESS))
                && (!status.equals(RIDE_STATUS_COMPLETED))
                && (!status.equals(RIDE_STATUS_CANCELLED))
                && (!status.equals(RIDE_STATUS_WAITING))
                && (!status.equals(RIDE_STATUS_PAID))) {
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
