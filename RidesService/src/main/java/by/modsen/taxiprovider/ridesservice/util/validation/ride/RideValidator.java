package by.modsen.taxiprovider.ridesservice.util.validation.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityValidateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Status.*;
import static by.modsen.taxiprovider.ridesservice.util.Message.*;
import static by.modsen.taxiprovider.ridesservice.util.PaymentType.*;

@Component
@Slf4j
public class RideValidator {

    private final List<String> rideStatuses = getRideStatuses();

    public void validate(Ride ride) throws EntityValidateException {
        String status = ride.getStatus();
        if (!rideStatuses.contains(status)) {
            log.info(RIDE_STATUS_IS_INVALID);
            throw new EntityValidateException(RIDE_STATUS_IS_INVALID);
        }

        if (ride.getPaymentType() != null) {
            String paymentType = ride.getPaymentType();
            if ((!paymentType.equals(PAYMENT_TYPE_CASH)) && (!paymentType.equals(PAYMENT_TYPE_CARD))) {
                log.info(PAYMENT_TYPE_IS_INVALID);
                throw new EntityValidateException(PAYMENT_TYPE_IS_INVALID);
            }
        }
    }
}
