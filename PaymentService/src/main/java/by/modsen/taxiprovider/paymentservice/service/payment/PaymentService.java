package by.modsen.taxiprovider.paymentservice.service.payment;

import by.modsen.taxiprovider.paymentservice.dto.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.model.User;
import by.modsen.taxiprovider.paymentservice.service.user.UsersService;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Token;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe-api-private-key}")
    private String STRIPE_API_PRIVATE_KEY;

    @Value("${stripe-api-public-key}")
    private String STRIPE_API_PUBLIC_KEY;

    private final UsersService usersService;

    @PostConstruct
    public void init(){
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
    }

    public Charge charge(ChargeRequestDTO chargeRequestDTO)
            throws PaymentException {

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();
        Charge charge;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", Math.round(chargeRequestDTO.getAmount().floatValue() * 100));
            params.put("currency", chargeRequestDTO.getCurrency());
            params.put("source", chargeRequestDTO.getCardToken());
            charge = Charge.create(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return charge;
    }

    public Token createStripeToken(CardRequestDTO cardRequestDTO) throws PaymentException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PUBLIC_KEY)
                .build();

        Token token;
        try {
            Map<String, Object> card = new HashMap<>();
            card.put("number", cardRequestDTO.getNumber());
            card.put("exp_month", cardRequestDTO.getMonth());
            card.put("exp_year", cardRequestDTO.getYear());
            card.put("cvc", cardRequestDTO.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put("card", card);
            token = Token.create(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return token;
    }

    public void createCustomer(CustomerDTO customerDTO) throws PaymentException {
        Stripe.apiKey = STRIPE_API_PUBLIC_KEY;

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(customerDTO.getName())
                        .setEmail(customerDTO.getEmail())
                        .setPhone(customerDTO.getPhone())
                        .setBalance((long) Math.round(customerDTO.getBalance().floatValue() * 100))
                        .build();
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;

        createUser(params, customerDTO.getPassengerId());
    }

    private void createUser(CustomerCreateParams params, long id) throws PaymentException {
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
        Customer customer = checkCustomerParams(params);
        createPaymentMethod(customer.getId());
        User user = User.builder()
                .customerId(customer.getId())
                .customerId(customer.getId())
                .passengerId(id)
                .build();
        usersService.save(user);
    }

    private Customer checkCustomerParams(CustomerCreateParams params) throws PaymentException {
        try {
            return Customer.create(params);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    private void createPaymentMethod(String customerId) throws PaymentException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();
        Map<String, Object> paymentMethodParams = new HashMap<>();
        paymentMethodParams.put("type", "card");
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("token", "tok_visa");
        paymentMethodParams.put("card", cardParams);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put("customer", customerId);
            paymentMethod.attach(attachParams, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

}
