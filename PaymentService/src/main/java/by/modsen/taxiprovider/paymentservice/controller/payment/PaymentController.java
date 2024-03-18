package by.modsen.taxiprovider.paymentservice.controller.payment;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerBalanceRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.BalanceResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDTO;
import by.modsen.taxiprovider.paymentservice.service.payment.PaymentService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<ChargeResponseDTO> charge(@RequestBody @Valid ChargeRequestDTO chargeRequestDTO,
                                        BindingResult bindingResult) throws PaymentException, EntityValidateException {
        return new ResponseEntity<>(paymentService.charge(chargeRequestDTO, bindingResult), HttpStatus.OK);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponseDTO> getToken(@RequestBody @Valid CardRequestDTO cardRequestDTO,
                                         BindingResult bindingResult) throws PaymentException, EntityValidateException {
        return new ResponseEntity<>(paymentService.createStripeToken(cardRequestDTO, bindingResult), HttpStatus.OK);
    }

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponseDTO> saveCustomer(@RequestBody @Valid CustomerDTO customerDTO,
                                                            BindingResult bindingResult) throws PaymentException,
            EntityValidateException, EntityNotFoundException {
        return new ResponseEntity<>(paymentService.createCustomer(customerDTO, bindingResult), HttpStatus.CREATED);
    }

    @PostMapping("/customerBalance")
    public ResponseEntity<BalanceResponseDTO> getCustomerBalance(@RequestBody @Valid CustomerBalanceRequestDTO customerBalanceRequestDTO,
                                                                 BindingResult bindingResult)
            throws PaymentException, EntityValidateException {
        return new ResponseEntity<>(paymentService.getCustomerBalance(customerBalanceRequestDTO, bindingResult),
                HttpStatus.OK);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDTO> editCustomer(@PathVariable("id") long id,
                                                   @RequestBody @Valid CustomerDTO customerDTO,
                                                   BindingResult bindingResult)
            throws PaymentException, EntityNotFoundException, EntityValidateException {
        return new ResponseEntity<>(paymentService.updateCustomer(id, customerDTO, bindingResult), HttpStatus.OK);
    }

    @PatchMapping("/customers/drivers/{id}")
    public ResponseEntity<CustomerResponseDTO> updateDriverBalance(@PathVariable("id") long id)
            throws PaymentException, EntityNotFoundException {
        return new ResponseEntity<>(paymentService.updateDriverBalance(id),
                HttpStatus.OK);
    }

    @DeleteMapping(value = "/customers/{id}", params = "role")
    public ResponseEntity<CustomerResponseDTO> deleteCustomer(@PathVariable("id") long id,
                                                     @RequestParam("role") String role) throws PaymentException,
            EntityNotFoundException {
        return new ResponseEntity<>(paymentService.deleteCustomer(id, role), HttpStatus.OK);
    }

    @PostMapping("/customerCharge")
    public ResponseEntity<ChargeResponseDTO> chargeByCustomer(@RequestBody @Valid CustomerChargeRequestDTO customerChargeRequestDTO,
                                                              BindingResult bindingResult)
            throws NotEnoughMoneyException, PaymentException, EntityNotFoundException, EntityValidateException {

        return new ResponseEntity<>(paymentService.chargeFromCustomer(customerChargeRequestDTO, bindingResult), HttpStatus.OK);
    }

}
