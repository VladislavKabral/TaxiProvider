package by.modsen.taxiprovider.ridesservice.service.ride.distance;

import by.modsen.taxiprovider.ridesservice.model.ride.Address;
import by.modsen.taxiprovider.ridesservice.util.exception.DistanceCalculationException;
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

@Component
public class DistanceCalculator {

    @Value("${api_uri}")
    private String API_URI;

    private static final String ROTES_NODE_NAME = "routes";

    private static final String DISTANCE_PARAM_NAME = "distance";

    private static final String STATUS_PARAM_NAME = "status";

    public int calculate(Address source, Address target) throws IOException, InterruptedException, ParseException, DistanceCalculationException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URI))
                .POST(HttpRequest.BodyPublishers
                        .ofString(getRequestBody(source, target)))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject jsonObject = (JSONObject) JSONValue.parseWithException(response.body());
        JSONArray routesNode = (JSONArray) JSONValue.parseWithException(jsonObject.get(ROTES_NODE_NAME).toString());
        Iterator routesIterator = routesNode.iterator();
        JSONObject route = (JSONObject) routesIterator.next();
        String status = route.get(STATUS_PARAM_NAME).toString();

        if (!status.equals("OK")) {
            throw new DistanceCalculationException("Impossible to calculate the distance of the ride");
        }

        return Integer.parseInt(route.get(DISTANCE_PARAM_NAME).toString());
    }

    private String getRequestBody(Address source, Address target) {
        return "{\n" +
                "    \"points\": [\n" +
                "        {\n" +
                "            \"lat\":" + source.getLatitude() + ",\n" +
                "            \"lon\":" + source.getLongitude() + "\n" +
                "        },\n" +
                "        {\n" +
                "            \"lat\":" + target.getLatitude() + ",\n" +
                "            \"lon\":" + target.getLongitude() + "\n" +
                "        }\n" +
                "    ],\n" +
                "    \"sources\": [0],\n" +
                "    \"targets\": [1]\n" +
                "}";
    }

}
