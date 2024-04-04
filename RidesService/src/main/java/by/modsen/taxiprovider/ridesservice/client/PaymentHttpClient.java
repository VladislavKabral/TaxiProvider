package by.modsen.taxiprovider.ridesservice.client;

import by.modsen.taxiprovider.ridesservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.ridesservice.dto.request.CustomerChargeRequestDto;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Component
public class PaymentHttpClient {

    @Value("${payment-service-host-url}")
    private String PAYMENT_SERVICE_HOST_URL;

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private static final int RETRY_DURATION_TIME = 5;

    public void payRide(CustomerChargeRequestDto chargeRequest) {
        WebClient webClient = WebClient.builder()
                .baseUrl(PAYMENT_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.post()
                .uri("/customerCharge")
                .bodyValue(chargeRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, PAYMENT_SERVICE_HOST_URL))))
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    PAYMENT_SERVICE_HOST_URL));
                        }))
                .block();
    }
}
