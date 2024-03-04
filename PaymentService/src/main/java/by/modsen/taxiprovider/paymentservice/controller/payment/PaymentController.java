package by.modsen.taxiprovider.paymentservice.controller.payment;

import by.modsen.taxiprovider.paymentservice.dto.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> charge(@RequestBody ChargeRequestDTO chargeRequestDTO) throws StripeException {
        PaymentIntent paymentIntent = paymentService.charge(chargeRequestDTO);

        return ResponseEntity.ok(paymentIntent.getStatus());
    }
}
