package by.modsen.taxiprovider.paymentservice.service.payment;

import by.modsen.taxiprovider.paymentservice.client.DriverHttpClient;
import by.modsen.taxiprovider.paymentservice.client.RideHttpClient;
import by.modsen.taxiprovider.paymentservice.dto.request.CardRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.request.ChargeRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.CustomerDto;
import by.modsen.taxiprovider.paymentservice.dto.request.CustomerChargeRequestDto;
import by.modsen.taxiprovider.paymentservice.dto.request.RideDto;
import by.modsen.taxiprovider.paymentservice.dto.response.BalanceResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.ChargeResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.CustomerResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.DriverDto;
import by.modsen.taxiprovider.paymentservice.dto.response.TokenResponseDto;
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
import org.springframework.stereotype.Service;

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

    private final DriverHttpClient driverHttpClient;

    private final RideHttpClient rideHttpClient;

    private static final int CONVERT_COEFFICIENT = 100;

    private static final double DRIVER_COMMISSION = 0.74;

    private static final String SUCCEED_CHARGE_STATUS_NAME = "SUCCEED";

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final String RIDE_STATUS_PAID = "PAID";

    private static final String CARD_NUMBER_FIELD_NAME = "number";

    private static final String EXPIRATION_MONTH_FIELD_NAME = "exp_month";

    private static final String EXPIRATION_YEAR_FIELD_NAME = "exp_year";

    private static final String CVC_FIELD_NAME = "cvc";

    private static final String CARD_FIELD_NAME = "card";

    private static final String AMOUNT_FIELD_NAME = "amount";

    private static final String CURRENCY_FIELD_NAME = "currency";

    private static final String SOURCE_FIELD_NAME = "source";

    private static final String PAYMENT_TYPE_NAME = "pm_card_visa";

    private static final String TYPE_FIELD_NAME = "type";

    private static final String CUSTOMER_FIELD_NAME = "customer";

    private static final String TOKEN_FIELD_NAME = "token";

    private static final String TOKEN_VALUE = "tok_visa";

    @PostConstruct
    public void init(){
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
    }

    public ChargeResponseDto charge(ChargeRequestDto chargeRequestDTO)
            throws PaymentException {

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PRIVATE_KEY)
                .build();

        Charge charge;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put(AMOUNT_FIELD_NAME, convertToStripeScale(chargeRequestDTO.getAmount().floatValue()));
            params.put(CURRENCY_FIELD_NAME, chargeRequestDTO.getCurrency());
            params.put(SOURCE_FIELD_NAME, chargeRequestDTO.getCardToken());
            charge = Charge.create(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        rideHttpClient.sendRequestForClosingRide(RideDto.builder()
                .driverId(chargeRequestDTO.getDriverId())
                .passengerId(chargeRequestDTO.getPassengerId())
                .status(RIDE_STATUS_PAID)
                .build());

        return ChargeResponseDto.builder()
                .currency(charge.getCurrency())
                .amount(BigDecimal.valueOf(charge.getAmount()))
                .status(SUCCEED_CHARGE_STATUS_NAME)
                .build();
    }

    public TokenResponseDto createStripeToken(CardRequestDto cardRequestDTO)
            throws PaymentException, EntityValidateException {

        cardRequestValidator.validate(cardRequestDTO);

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(STRIPE_API_PUBLIC_KEY)
                .build();

        Token token;
        try {
            Map<String, Object> card = new HashMap<>();
            card.put(CARD_NUMBER_FIELD_NAME, cardRequestDTO.getNumber());
            card.put(EXPIRATION_MONTH_FIELD_NAME, cardRequestDTO.getMonth());
            card.put(EXPIRATION_YEAR_FIELD_NAME, cardRequestDTO.getYear());
            card.put(CVC_FIELD_NAME, cardRequestDTO.getCvc());
            Map<String, Object> params = new HashMap<>();
            params.put(CARD_FIELD_NAME, card);
            token = Token.create(params, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }

        return TokenResponseDto.builder()
                .token(token.getId())
                .build();
    }

    public CustomerResponseDto createCustomer(CustomerDto customerDTO) throws PaymentException, EntityValidateException,
            EntityNotFoundException {

        customerValidator.validate(customerDTO);

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
        return new CustomerResponseDto(customer.getId());
    }

    public CustomerResponseDto updateCustomer(long id, CustomerDto customerDTO) throws PaymentException,
            EntityNotFoundException {

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

        return new CustomerResponseDto(resource.getId());
    }

    public CustomerResponseDto deleteCustomer(long id, String role) throws EntityNotFoundException, PaymentException {
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

        return new CustomerResponseDto(customer.getId());
    }

    public ChargeResponseDto chargeFromCustomer(CustomerChargeRequestDto customerChargeRequestDTO)
            throws EntityNotFoundException, NotEnoughMoneyException, PaymentException {

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

        return ChargeResponseDto.builder()
                .currency(intent.getCurrency())
                .amount(BigDecimal.valueOf(intent.getAmount()))
                .status(SUCCEED_CHARGE_STATUS_NAME)
                .build();
    }

    private void confirmPaymentIntent(PaymentIntent resource) throws StripeException {
        PaymentIntentConfirmParams params =
                PaymentIntentConfirmParams.builder()
                        .setPaymentMethod(PAYMENT_TYPE_NAME)
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

    public CustomerResponseDto updateDriverBalance(long driverId) throws PaymentException, EntityNotFoundException {

        DriverDto driverDTO = driverHttpClient.getDriver(driverId);

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
        driverHttpClient.updateDriver(driverDTO);

        return new CustomerResponseDto(customer.getId());
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
        paymentMethodParams.put(TYPE_FIELD_NAME, CARD_FIELD_NAME);
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put(TOKEN_FIELD_NAME, TOKEN_VALUE);
        paymentMethodParams.put(CARD_FIELD_NAME, cardParams);
        try {
            PaymentMethod paymentMethod = PaymentMethod.create(paymentMethodParams);
            Map<String, Object> attachParams = new HashMap<>();
            attachParams.put(CUSTOMER_FIELD_NAME, customerId);
            paymentMethod.attach(attachParams, requestOptions);
        } catch (StripeException stripeException) {
            throw new PaymentException(stripeException.getMessage());
        }
    }

    public BalanceResponseDto getCustomerBalance(String customerId)
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

        return BalanceResponseDto
                .builder()
                .amount(BigDecimal.valueOf(customerBalanceTransactions.getData().get(0).getAmount() / CONVERT_COEFFICIENT))
                .currency(customerBalanceTransactions.getData().get(0).getCurrency())
                .build();

    }

    private long convertToStripeScale(float value) {
        return Math.round(value * CONVERT_COEFFICIENT);
    }
}
