package by.modsen.taxiprovider.ridesservice.client;

import by.modsen.taxiprovider.ridesservice.dto.driver.DriverDto;
import by.modsen.taxiprovider.ridesservice.dto.driver.DriverListDto;
import by.modsen.taxiprovider.ridesservice.dto.error.ErrorResponseDto;
import by.modsen.taxiprovider.ridesservice.util.exception.EntityNotFoundException;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceRequestException;
import by.modsen.taxiprovider.ridesservice.util.exception.ExternalServiceUnavailableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Component
public class DriverHttpClient {

    @Value("${drivers-service-host-url}")
    private String DRIVERS_SERVICE_HOST_URL;

    private static final int MAX_RETRY_ATTEMPTS = 3;

    private static final int RETRY_DURATION_TIME = 5;

    public DriverListDto getFreeDrivers() throws EntityNotFoundException {
        WebClient webClient = WebClient.builder()
                .filter(new ServletBearerExchangeFilterFunction())
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        DriverListDto drivers =  webClient.get()
                .uri("/free")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, DRIVERS_SERVICE_HOST_URL))))
                .bodyToMono(DriverListDto.class)
                .retryWhen(Retry.backoff(MAX_RETRY_ATTEMPTS, Duration.ofSeconds(RETRY_DURATION_TIME))
                        .filter(throwable -> throwable instanceof ExternalServiceRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ExternalServiceUnavailableException(String.format(
                                    CANNOT_GET_RESPONSE_FROM_EXTERNAL_SERVICE,
                                    DRIVERS_SERVICE_HOST_URL));
                        }))
                .block();

        if (drivers != null && drivers.getContent().isEmpty()) {
            throw new EntityNotFoundException(NO_FREE_DRIVERS);
        }

        return drivers;
    }

    public DriverDto getDriverById(long driverId) {
        WebClient webClient = WebClient.builder()
                .filter(new ServletBearerExchangeFilterFunction())
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(String.format("/%d", driverId))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, DRIVERS_SERVICE_HOST_URL))))
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
                .filter(new ServletBearerExchangeFilterFunction())
                .baseUrl(DRIVERS_SERVICE_HOST_URL)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();

        webClient.patch()
                .uri(String.format("/%d", driverDTO.getId()))
                .bodyValue(driverDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        clientResponse.bodyToMono(ErrorResponseDto.class)
                                .map(body -> new ExternalServiceRequestException(body.getMessage())))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
                        Mono.error(new ExternalServiceRequestException(String
                                .format(EXTERNAL_SERVICE_ERROR, DRIVERS_SERVICE_HOST_URL))))
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
