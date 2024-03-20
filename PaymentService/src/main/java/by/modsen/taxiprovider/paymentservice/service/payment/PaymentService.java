package by.modsen.taxiprovider.paymentservice.service.payment;

import by.modsen.taxiprovider.paymentservice.dto.error.ErrorResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDTO;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.BalanceResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.DriverDTO;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDTO;
import by.modsen.taxiprovider.paymentservice.model.User;
import by.modsen.taxiprovider.paymentservice.service.user.UsersService;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.paymentservice.util.exception.EntityValidateException;
import by.modsen.taxiprovider.paymentservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.paymentservice.util.exception.NotEnoughMoneyException;
import by.modsen.taxiprovider.paymentservice.util.exception.PaymentException;
import by.modsen.taxiprovider.paymentservice.util.validation.CardRequestValidator;
import by.modsen.taxiprovider.paymentservice.util.validation.CustomerValidator;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.CustomerBalanceTransactionCollection;
import com.stripe.model.Token;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.net.RequestOptions;
import com.stripe.param.CustomerBalanceTransactionsParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    private static final double DRIVER_COMMISSION = 0.74;

    private static final String SUCCEED_CHARGE_STATUS_NAME = "SUCCEED";

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    @Value("${drivers-service-host-url}")
    private String DRIVER_SERVICE_HOST_URL;

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
            params.put("amount", Math.round(chargeRequestDTO.getAmount().floatValue() * CONVERT_COEFFICIENT));
            params.put("currency", chargeRequestDTO.getCurrency());
            params.put("source", chargeRequestDTO.getCardToken());
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
                        .setBalance((long) Math.round(customerDTO.getBalance().floatValue() * CONVERT_COEFFICIENT))
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
                    .setBalance((long) Math.round(customerDTO.getBalance().floatValue() * CONVERT_COEFFICIENT))
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
        checkBalance(customerId, Math.round(customerChargeRequestDTO.getAmount().floatValue() * CONVERT_COEFFICIENT));
        updateBalance(customerId, Math.round(customerChargeRequestDTO.getAmount().floatValue() * CONVERT_COEFFICIENT));

        PaymentIntent intent;
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) Math.round(customerChargeRequestDTO.getAmount().floatValue() * CONVERT_COEFFICIENT))
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

    public CustomerResponseDTO updateDriverBalance(long driverId) throws PaymentException, EntityNotFoundException {

        DriverDTO driverDTO = getDriver(driverId);

        User user = usersService.findByTaxiUserIdAndRole(driverId, DRIVER_ROLE_NAME);
        BigDecimal amount = BigDecimal.valueOf(driverDTO.getBalance().floatValue() * DRIVER_COMMISSION);

        Customer customer;
        try {
            customer = Customer.retrieve(user.getCustomerId());
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .setBalance(customer.getBalance() + (long)(amount.floatValue() * CONVERT_COEFFICIENT))
                .build();

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        try {
            customer.update(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        driverDTO.setBalance(BigDecimal.ZERO);
        updateDriver(driverDTO);

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

    private DriverDTO getDriver(long driverId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVER_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(String.format("/%d", driverId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(errorResponseDTO -> new ExternalServiceRequestException(errorResponseDTO.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(DriverDTO.class)
                .block();
    }

    private void updateDriver(DriverDTO driverDTO) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVER_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.patch()
                .uri(String.format("/%d", driverDTO.getId()))
                .bodyValue(driverDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDTO.class)
                                .map(errorResponseDTO -> new ExternalServiceRequestException(errorResponseDTO.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(String.class)
                .block();
    }

    public BalanceResponseDTO getCustomerBalance(String customerId)
            throws PaymentException{
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;

        Customer resource;
        try {
            resource = Customer.retrieve(customerId);
        } catch (StripeException exception) {
            throw new PaymentException(exception.getMessage());
        }

        CustomerBalanceTransactionsParams params =
                CustomerBalanceTransactionsParams.builder().setLimit(1L).build();

        CustomerBalanceTransactionCollection customerBalanceTransactions;
        try {
            customerBalanceTransactions =
                    resource.balanceTransactions(params);
        } catch (StripeException exception) {
            throw new PaymentException(exception.getMessage());
        }

        return BalanceResponseDTO
                .builder()
                .amount(BigDecimal.valueOf(customerBalanceTransactions.getData().get(0).getAmount() / CONVERT_COEFFICIENT))
                .currency(customerBalanceTransactions.getData().get(0).getCurrency())
                .build();

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
