package by.modsen.taxiprovider.paymentservice.controller.payment;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.DriverBalanceRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
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
    public ResponseEntity<HttpStatus> saveCustomer(@RequestBody @Valid CustomerDTO customerDTO,
                                        BindingResult bindingResult) throws PaymentException, EntityValidateException {
        paymentService.createCustomer(customerDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/customers/{id}")
    public ResponseEntity<HttpStatus> editCustomer(@PathVariable("id") long id,
                                                   @RequestBody @Valid CustomerDTO customerDTO,
                                                   BindingResult bindingResult)
            throws PaymentException, EntityNotFoundException, EntityValidateException {
        paymentService.updateCustomer(id, customerDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/customers/drivers/{id}")
    public ResponseEntity<HttpStatus> updateDriverBalance(@PathVariable("id") long id,
                                                          @RequestBody @Valid DriverBalanceRequestDTO balanceRequestDTO,
                                                          BindingResult bindingResult)
            throws PaymentException, EntityValidateException, EntityNotFoundException {
        paymentService.updateDriverBalance(id, balanceRequestDTO, bindingResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<HttpStatus> deleteCustomer(@PathVariable("id") long id) throws PaymentException,
            EntityNotFoundException {

        paymentService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/customerCharge")
    public ResponseEntity<ChargeResponseDTO> chargeByCustomer(@RequestBody @Valid CustomerChargeRequestDTO customerChargeRequestDTO,
                                                              BindingResult bindingResult)
            throws NotEnoughMoneyException, PaymentException, EntityNotFoundException, EntityValidateException {

        return new ResponseEntity<>(paymentService.chargeFromCustomer(customerChargeRequestDTO, bindingResult), HttpStatus.OK);
    }

}
