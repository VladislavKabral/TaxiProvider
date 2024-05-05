package by.modsen.taxiprovider.paymentservice.client;

import by.modsen.taxiprovider.paymentservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.request.RideDto;
import by.modsen.taxiprovider.paymentservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.paymentservice.util.exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

@Component
public class RideHttpClient {

    @Value("${rides-service-host-url}")
    private String RIDES_SERVICE_HOST_URL;

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private static final int RETRY_DURATION_TIME = 5;

    public void sendRequestForClosingRide(RideDto rideDTO) {
        WebClient webClient = WebClient.builder()
                .filter(new ServletBearerExchangeFilterFunction())
                .baseUrl(RIDES_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.patch()
                .bodyValue(rideDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(errorResponseDto -> new ExternalServiceRequestException(errorResponseDto.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    RIDES_SERVICE_HOST_URL));
                        }))
                .block();
    }
}
