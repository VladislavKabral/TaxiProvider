package by.modsen.taxiprovider.paymentservice.service.payment;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDTO;
import by.modsen.taxiprovider.paymentservice.model.User;
import by.modsen.taxiprovider.paymentservice.service.user.UsersService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    public ChargeResponseDTO charge(ChargeRequestDTO chargeRequestDTO)
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

        return ChargeResponseDTO.builder()
                .currency(charge.getCurrency())
                .amount(BigDecimal.valueOf(charge.getAmount()))
                .status(charge.getStatus())
                .build();
    }

    public TokenResponseDTO createStripeToken(CardRequestDTO cardRequestDTO) throws PaymentException {
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

        return TokenResponseDTO.builder()
                .token(token.getId())
                .build();
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
        createUser(params, customerDTO.getTaxiUserId());
    }

    public void updateCustomer(long id, CustomerDTO customerDTO) throws PaymentException, EntityNotFoundException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        User user = usersService.findById(id);

        try {
            Customer resource = Customer.retrieve(user.getCustomerId());
            CustomerUpdateParams params = CustomerUpdateParams.builder()
                    .setName(customerDTO.getName())
                    .setEmail(customerDTO.getEmail())
                    .setPhone(customerDTO.getPhone())
                    .setBalance((long) Math.round(customerDTO.getBalance().floatValue() * 100))
                    .build();
            resource.update(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    public void deleteCustomer(long id) throws EntityNotFoundException, PaymentException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        User user = usersService.findById(id);
        usersService.delete(user.getId());

        Customer customer;
        try {
            customer = Customer.retrieve(user.getCustomerId(), requestOptions);
            customer.delete();
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

    }

    public ChargeResponseDTO chargeFromCustomer(CustomerChargeRequestDTO customerChargeRequestDTO)
            throws EntityNotFoundException, NotEnoughMoneyException, PaymentException {

        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
        User user = usersService.findById(customerChargeRequestDTO.getTaxiUserId());
        String customerId = user.getCustomerId();
        checkBalance(customerId, Math.round(customerChargeRequestDTO.getAmount().floatValue() * 100));
        updateBalance(customerId, Math.round(customerChargeRequestDTO.getAmount().floatValue() * 100));

        PaymentIntent intent;
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) Math.round(customerChargeRequestDTO.getAmount().floatValue() * 100))
                    .setCurrency(customerChargeRequestDTO.getCurrency())
                    .setCustomer(customerId)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .setAllowRedirects(
                                            PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER
                                    )
                                    .build()
                    )
                    .build();
            intent = PaymentIntent.create(params);
            confirmPaymentIntent(intent);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return ChargeResponseDTO.builder()
                .currency(intent.getCurrency())
                .amount(BigDecimal.valueOf(intent.getAmount()))
                .status(intent.getStatus())
                .build();
    }

    private void confirmPaymentIntent(PaymentIntent resource) throws StripeException {
        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod("pm_card_visa")
                        .build();
        resource.confirm(params);
    }

    private void updateBalance(String customerId, long amount) throws PaymentException {
        Customer customer;
        try {
            customer = Customer.retrieve(customerId);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .setBalance(customer.getBalance() - amount)
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        try {
            customer.update(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    private void checkBalance(String customerId, long amount) throws NotEnoughMoneyException, PaymentException {
        Customer customer;
        try {
            customer = Customer.retrieve(customerId);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        Long balance = customer.getBalance();
        if (balance < amount) {
            throw new NotEnoughMoneyException("Not enough money on the balance");
        }
    }

    private void createUser(CustomerCreateParams params, long id) throws PaymentException {
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
        Customer customer = checkCustomerParams(params);
        createPaymentMethod(customer.getId());
        User user = User.builder()
                .customerId(customer.getId())
                .customerId(customer.getId())
                .taxiUserId(id)
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
