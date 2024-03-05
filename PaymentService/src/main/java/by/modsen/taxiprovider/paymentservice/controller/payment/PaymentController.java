package by.modsen.taxiprovider.paymentservice.controller.payment;

import by.modsen.taxiprovider.paymentservice.dto.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.service.payment.PaymentService;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/charge")
    public ResponseEntity<String> charge(@RequestBody ChargeRequestDTO chargeRequestDTO) throws Exception {
        Charge charge = paymentService.charge(chargeRequestDTO);

        return ResponseEntity.ok(charge.getStatus());
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody CardRequestDTO cardRequestDTO) throws Exception {
        Token token = paymentService.createStripeToken(cardRequestDTO);

        return ResponseEntity.ok(token.getId());
    }

    @PostMapping("/customers")
    public ResponseEntity<HttpStatus> saveCustomer(@RequestBody CustomerDTO customerDTO) throws PaymentException {
        paymentService.createCustomer(customerDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
