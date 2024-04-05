package by.modsen.taxiprovider.driverservice.integration.mock;

import by.modsen.taxiprovider.driverservice.dto.response.RatingResponseDto;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;

import static by.modsen.taxiprovider.driverservice.utilily.DriversTestUtil.*;
import static org.springframework.util.StreamUtils.copyToString;

public class ResponseMocks {

    public static void setupMockRatingServiceResponse(WireMockServer wireMockServer) throws IOException {
        wireMockServer.stubFor(WireMock.post(WireMock.urlMatching(INIT_DRIVER_RATING_PATH))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(
                                copyToString(
                                        RatingResponseDto.class.getClassLoader().getResourceAsStream("response/init-driver-rating.json"),
                                        Charset.defaultCharset()))));

    }
}
