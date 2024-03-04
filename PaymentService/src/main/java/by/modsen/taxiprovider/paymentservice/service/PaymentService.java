package by.modsen.taxiprovider.paymentservice.service;

import by.modsen.taxiprovider.paymentservice.dto.ChargeRequestDTO;
import by.modsen.taxiprovider.paymentservice.mapper.ChargeRequestMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TokenCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe-api-private-key}")
    private String STRIPE_API_PRIVATE_KEY;

    private final ChargeRequestMapper chargeRequestMapper;

    @PostConstruct
    public void init(){
        Stripe.apiKey = STRIPE_API_PRIVATE_KEY;
    }

    public PaymentIntent charge(ChargeRequestDTO chargeRequestDTO)
            throws StripeException {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(10L)
                        .setCurrency("usd")
                        .setPaymentMethod("pm_card_visa")
                        .build();


        return PaymentIntent.create(params);
    }

    private Token createStripeToken() throws StripeException {
        TokenCreateParams params = TokenCreateParams.builder()
                .setCard(
                        TokenCreateParams.Card.builder()
                                .setNumber("4000001120000005")
                                .setExpMonth("12")
                                .setExpYear("2025")
                                .setCvc("314")
                                .build())
                .build();


        return Token.create(params);
    }

}
