package by.modsen.taxiprovider.ridesservice.service.ride.distance;

import by.modsen.taxiprovider.ridesservice.dto.ride.DistanceRequestDTO;
import by.modsen.taxiprovider.ridesservice.mapper.ride.AddressMapper;
import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.List;

import static by.modsen.taxiprovider.ridesservice.util.Message.*;

@Component
@RequiredArgsConstructor
public class DistanceCalculator {

    @Value("${api_uri}")
    private String API_URI;

    private final AddressMapper addressMapper;

    private static final String ROTES_NODE_NAME = "routes";

    private static final String DISTANCE_PARAM_NAME = "distance";

    private static final String STATUS_PARAM_NAME = "status";

    public int calculate(Address source, Address target) throws IOException, InterruptedException, ParseException, DistanceCalculationException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URI))
                .POST(HttpRequest.BodyPublishers.ofString(getRequestBody(source, target)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(response.body());
        JSONArray routesNode = (JSONArray) JSONValue.parseWithException(jsonObject.get(ROTES_NODE_NAME).toString());
        Iterator routesIterator = routesNode.iterator();
        JSONObject route = (JSONObject) routesIterator.next();
        String status = route.get(STATUS_PARAM_NAME).toString();

        if (!status.equals("OK")) {
            throw new DistanceCalculationException(DISTANCE_CALCULATION_IS_FAILED);
        }

        return Integer.parseInt(route.get(DISTANCE_PARAM_NAME).toString());
    }

    private String getRequestBody(Address source, Address target) throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(DistanceRequestDTO.builder()
                .points(addressMapper.toListDTO(List.of(source, target)))
                .sources(new int[]{0})
                .targets(new int[]{1})
                .build());
    }

}
