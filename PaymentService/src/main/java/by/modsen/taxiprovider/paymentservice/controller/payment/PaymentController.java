package by.modsen.taxiprovider.paymentservice.controller.payment;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDTO;
import by.modsen.taxiprovider.paymentservice.service.payment.PaymentService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponseDTO> charge(@RequestBody ChargeRequestDTO chargeRequestDTO) throws Exception {
        return new ResponseEntity<>(paymentService.charge(chargeRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> getToken(@RequestBody CardRequestDTO cardRequestDTO) throws Exception {
        return new ResponseEntity<>(paymentService.createStripeToken(cardRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<HttpStatus> saveCustomer(@RequestBody CustomerDTO customerDTO) throws PaymentException {
        paymentService.createCustomer(customerDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<HttpStatus> editCustomer(@PathVariable("id") long id, @RequestBody CustomerDTO customerDTO)
            throws PaymentException, EntityNotFoundException {

        paymentService.updateCustomer(id, customerDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) throws PaymentException,
            EntityNotFoundException {

        paymentService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/customerCharge")
    public ResponseEntity<ChargeResponseDTO> chargeByCustomer(@RequestBody CustomerChargeRequestDTO customerChargeRequestDTO)
            throws NotEnoughMoneyException, PaymentException, EntityNotFoundException {

        return new ResponseEntity<>(paymentService.chargeFromCustomer(customerChargeRequestDTO), HttpStatus.OK);
    }

}
