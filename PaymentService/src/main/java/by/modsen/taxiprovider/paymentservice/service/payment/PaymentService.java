package by.modsen.taxiprovider.paymentservice.service.payment;

import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.DriverBalanceRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDTO;
import by.modsen.taxiprovider.paymentservice.model.User;
import by.modsen.taxiprovider.paymentservice.service.user.UsersService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import by.modsen.taxiprovider.paymentservice.util.validation.CardRequestValidator;
import by.modsen.taxiprovider.paymentservice.util.validation.CustomerValidator;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

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

    private final CardRequestValidator cardRequestValidator;

    private final CustomerValidator customerValidator;

    private static final int CONVERT_COEFFICIENT = 100;
    private static final String SUCCEED_CHARGE_STATUS_NAME = "SUCCEED";
    private static final String DRIVER_ROLE_NAME = "DRIVER";
    private static final String AMOUNT_PROPERTY_NAME = "amount";
    private static final String CURRENCY_PROPERTY_NAME = "currency";
    private static final String SOURCE_PROPERTY_NAME = "source";
    private static final String PAYMENT_METHOD_PROPERTY_NAME = "pm_card_visa";
    private static final String CARD_NUMBER_PROPERTY_NAME = "number";
    private static final String EXPIRATION_MONTH_PROPERTY_NAME = "exp_month";
    private static final String EXPIRATION_YEAR_PROPERTY_NAME = "exp_year";
    private static final String CVC_PROPERTY_NAME = "cvc";
    private static final String CARD_PROPERTY_NAME = "card";
    private static final String PAYMENT_TYPE_PROPERTY_NAME = "type";
    private static final String TOKEN_PROPERTY_NAME = "token";
    private static final String TEST_TOKEN_VALUE = "tok_visa";
    private static final String CUSTOMER_PROPERTY_NAME = "customer";

    @PostConstruct
    public void init(){
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
    }

    public ChargeResponseDTO charge(ChargeRequestDTO chargeRequestDTO, BindingResult bindingResult)
            throws PaymentException, EntityValidateException {

        handleBindingResult(bindingResult);

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        Charge charge;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(AMOUNT_PROPERTY_NAME, convertToStripeScale(chargeRequestDTO.getAmount().floatValue()));
            params.put(CURRENCY_PROPERTY_NAME, chargeRequestDTO.getCurrency());
            params.put(SOURCE_PROPERTY_NAME, chargeRequestDTO.getCardToken());
            charge = Charge.create(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return ChargeResponseDTO.builder()
                .currency(charge.getCurrency())
                .amount(BigDecimal.valueOf(charge.getAmount()))
                .status(SUCCEED_CHARGE_STATUS_NAME)
                .build();
    }

    public TokenResponseDTO createStripeToken(CardRequestDTO cardRequestDTO, BindingResult bindingResult)
            throws PaymentException, EntityValidateException {

        cardRequestValidator.validate(cardRequestDTO, bindingResult);
        handleBindingResult(bindingResult);

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PUBLIC_KEY)
                .build();

        Token token;
        try {
            Map<String, Object> card = new HashMap<>();
            card.put(CARD_NUMBER_PROPERTY_NAME, cardRequestDTO.getNumber());
            card.put(EXPIRATION_MONTH_PROPERTY_NAME, cardRequestDTO.getMonth());
            card.put(EXPIRATION_YEAR_PROPERTY_NAME, cardRequestDTO.getYear());
            card.put(CVC_PROPERTY_NAME, cardRequestDTO.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put(CARD_PROPERTY_NAME, card);
            token = Token.create(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return TokenResponseDTO.builder()
                .token(token.getId())
                .build();
    }

    public CustomerResponseDTO createCustomer(CustomerDTO customerDTO, BindingResult bindingResult)
            throws PaymentException, EntityValidateException, EntityNotFoundException {

        customerValidator.validate(customerDTO, bindingResult);
        handleBindingResult(bindingResult);

        Stripe.apiKey = STRIPE_API_PUBLIC_KEY;

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .setName(customerDTO.getName())
                        .setEmail(customerDTO.getEmail())
                        .setPhone(customerDTO.getPhone())
                        .setBalance(convertToStripeScale(customerDTO.getBalance().floatValue()))
                        .build();

        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
        createUser(params, customerDTO.getTaxiUserId(), customerDTO.getRole());

        Customer customer = checkCustomerParams(params);
        if (customer == null) {
            throw new EntityNotFoundException(String.format(CUSTOMER_NOT_FOUND, customerDTO.getEmail()));
        }
        return new CustomerResponseDTO(customer.getId());
    }

    public CustomerResponseDTO updateCustomer(long id, CustomerDTO customerDTO, BindingResult bindingResult) throws PaymentException,
            EntityNotFoundException, EntityValidateException {

        handleBindingResult(bindingResult);

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        User user = usersService.findByTaxiUserIdAndRole(id, customerDTO.getRole());

        Customer resource;
        try {
            resource = Customer.retrieve(user.getCustomerId());
            CustomerUpdateParams params = CustomerUpdateParams.builder()
                    .setName(customerDTO.getName())
                    .setEmail(customerDTO.getEmail())
                    .setPhone(customerDTO.getPhone())
                    .setBalance(convertToStripeScale(customerDTO.getBalance().floatValue()))
                    .build();
            resource.update(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return new CustomerResponseDTO(resource.getId());
    }

    public CustomerResponseDTO deleteCustomer(long id, String role) throws EntityNotFoundException, PaymentException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        User user = usersService.findByTaxiUserIdAndRole(id, role);
        usersService.delete(user.getId(), role);

        Customer customer;
        try {
            customer = Customer.retrieve(user.getCustomerId(), requestOptions);
            customer.delete();
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return new CustomerResponseDTO(customer.getId());
    }

    public ChargeResponseDTO chargeFromCustomer(CustomerChargeRequestDTO customerChargeRequestDTO,
                                                BindingResult bindingResult)
            throws EntityNotFoundException, NotEnoughMoneyException, PaymentException, EntityValidateException {

        handleBindingResult(bindingResult);

        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
        User user = usersService.findByTaxiUserIdAndRole(customerChargeRequestDTO.getTaxiUserId(),
                customerChargeRequestDTO.getRole());
        String customerId = user.getCustomerId();
        checkBalance(customerId, convertToStripeScale(customerChargeRequestDTO.getAmount().floatValue()));
        updateBalance(customerId, convertToStripeScale(customerChargeRequestDTO.getAmount().floatValue()));

        PaymentIntent intent;
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(convertToStripeScale(customerChargeRequestDTO.getAmount().floatValue()))
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
                .status(SUCCEED_CHARGE_STATUS_NAME)
                .build();
    }

    private void confirmPaymentIntent(PaymentIntent resource) throws StripeException {
        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod(PAYMENT_METHOD_PROPERTY_NAME)
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

    public CustomerResponseDTO updateDriverBalance(long driverId, DriverBalanceRequestDTO driverBalanceRequestDTO,
                                    BindingResult bindingResult) throws PaymentException, EntityNotFoundException,
            EntityValidateException {

        handleBindingResult(bindingResult);

        User driver = usersService.findByTaxiUserIdAndRole(driverId, DRIVER_ROLE_NAME);
        BigDecimal amount = driverBalanceRequestDTO.getAmount();

        Customer customer;
        try {
            customer = Customer.retrieve(driver.getCustomerId());
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .setBalance(customer.getBalance() + convertToStripeScale(amount.floatValue()))
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        try {
            customer.update(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return new CustomerResponseDTO(customer.getId());
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
            throw new NotEnoughMoneyException(NOT_ENOUGH_MONEY_ON_BALANCE);
        }
    }

    private void createUser(CustomerCreateParams params, long id, String role) throws PaymentException {
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
        Customer customer = checkCustomerParams(params);
        createPaymentMethod(customer.getId());
        User user = User.builder()
                .customerId(customer.getId())
                .customerId(customer.getId())
                .taxiUserId(id)
                .role(role)
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
        paymentMethodParams.put(PAYMENT_TYPE_PROPERTY_NAME, CARD_PROPERTY_NAME);
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put(TOKEN_PROPERTY_NAME, TEST_TOKEN_VALUE);
        paymentMethodParams.put(CARD_PROPERTY_NAME, cardParams);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put(CUSTOMER_PROPERTY_NAME, customerId);
            paymentMethod.attach(attachParams, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    private long convertToStripeScale(float value) {
        return Math.round(value * CONVERT_COEFFICIENT);
    }

    private void handleBindingResult(BindingResult bindingResult) throws EntityValidateException {
        if (bindingResult.hasErrors()) {
            StringBuilder message = new StringBuilder();

            for (FieldError error: bindingResult.getFieldErrors()) {
                message.append(error.getDefaultMessage()).append(". ");
            }

            throw new EntityValidateException(message.toString());
        }
    }
}
