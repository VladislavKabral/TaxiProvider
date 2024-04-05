package by.modsen.taxiprovider.paymentservice.controller.payment;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDto;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.response.BalanceResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDto;
import by.modsen.taxiprovider.paymentservice.service.payment.PaymentService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import jakarta.validation.Valid;
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
    public ResponseEntity<ChargeResponseDto> charge(@RequestBody @Valid ChargeRequestDto chargeRequestDTO)
            throws PaymentException {
        return new ResponseEntity<>(paymentService.charge(chargeRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDto> getToken(@RequestBody @Valid CardRequestDto cardRequestDTO)
            throws PaymentException, EntityValidateException {
        return new ResponseEntity<>(paymentService.createStripeToken(cardRequestDTO), HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponseDto> saveCustomer(@RequestBody @Valid CustomerDto customerDTO)
            throws PaymentException, EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(paymentService.createCustomer(customerDTO), HttpStatus.CREATED);
    }

    @GetMapping("/customers/{id}/balance")
    public ResponseEntity<BalanceResponseDto> getCustomerBalance(@PathVariable("id") String customerId)
            throws PaymentException {
        return new ResponseEntity<>(paymentService.getCustomerBalance(customerId), HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDto> editCustomer(@PathVariable("id") long id,
                                                            @RequestBody @Valid CustomerDto customerDTO)
            throws PaymentException, EntityNotFoundException {
        return new ResponseEntity<>(paymentService.updateCustomer(id, customerDTO), HttpStatus.OK);
    }

    @PatchMapping("/customers/drivers/{id}")
    public ResponseEntity<CustomerResponseDto> updateDriverBalance(@PathVariable("id") long id)
            throws PaymentException, EntityNotFoundException {
        return new ResponseEntity<>(paymentService.updateDriverBalance(id),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "/customers/{id}", params = "role")
    public ResponseEntity<CustomerResponseDto> deleteCustomer(@PathVariable("id") long id,
                                                              @RequestParam("role") String role) throws PaymentException,
            EntityNotFoundException {
        return new ResponseEntity<>(paymentService.deleteCustomer(id, role), HttpStatus.OK);
    }

    @PostMapping("/customerCharge")
    public ResponseEntity<ChargeResponseDto> chargeByCustomer(@RequestBody @Valid CustomerChargeRequestDto customerChargeRequestDTO)
            throws NotEnoughMoneyException, PaymentException, EntityNotFoundException {
        return new ResponseEntity<>(paymentService.chargeFromCustomer(customerChargeRequestDTO), HttpStatus.OK);
    }

}
