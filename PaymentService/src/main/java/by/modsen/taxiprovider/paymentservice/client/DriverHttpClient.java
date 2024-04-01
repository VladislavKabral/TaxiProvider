package by.modsen.taxiprovider.paymentservice.client;

import by.modsen.taxiprovider.paymentservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.paymentservice.dto.response.DriverDto;
import by.modsen.taxiprovider.paymentservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.paymentservice.util.exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static by.modsen.taxiprovider.paymentservice.util.Message.*;

@Component
public class DriverHttpClient {

    @Value("${drivers-service-host-url}")
    private String DRIVERS_SERVICE_HOST_URL;

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private static final int RETRY_DURATION_TIME = 5;

    public DriverDto getDriver(long driverId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(String.format("/%d", driverId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(errorResponseDto -> new ExternalServiceRequestException(errorResponseDto.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(EXTERNAL_SERVICE_ERROR)))
                .bodyToMono(DriverDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    DRIVERS_SERVICE_HOST_URL));
                        }))
                .block();
    }

    public void updateDriver(DriverDto driverDTO) {
        WebClient webClient = WebClient.builder()
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.patch()
                .uri(String.format("/%d", driverDTO.getId()))
                .bodyValue(driverDTO)
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
                                    DRIVERS_SERVICE_HOST_URL));
                        }))
                .block();
    }
}
