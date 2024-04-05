package by.modsen.taxiprovider.driverservice.client;

import by.modsen.taxiprovider.driverservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.driverservice.dto.rating.RatingDto;
import by.modsen.taxiprovider.driverservice.dto.request.DriverRatingRequestDto;
import by.modsen.taxiprovider.driverservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.driverservice.util.exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static by.modsen.taxiprovider.driverservice.util.Message.*;

@Component
public class RatingHttpClient {

    private static final String DRIVER_ROLE_NAME = "DRIVER";
    private static final String TAXI_USER_ID_FIELD_NAME = "taxiUserId";
    private static final String ROLE_FIELD_NAME = "role";

    @Value("${ratings-service-host-url}")
    private String RATINGS_SERVICE_HOST_URL;

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private static final int RETRY_DURATION_TIME = 5;

    public void initDriverRating(long driverId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(RATINGS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.post()
                .uri("/init")
                .bodyValue(DriverRatingRequestDto.builder()
                        .taxiUserId(driverId)
                        .role(DRIVER_ROLE_NAME)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    RATINGS_SERVICE_HOST_URL));
                        }))
                .block();
    }

    public RatingDto getDriverRating(long driverId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(RATINGS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam(TAXI_USER_ID_FIELD_NAME, driverId)
                        .queryParam(ROLE_FIELD_NAME, DRIVER_ROLE_NAME)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(errorResponseDto -> new ExternalServiceRequestException(errorResponseDto.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(RatingDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    RATINGS_SERVICE_HOST_URL));
                        }))
                .block();
    }
}