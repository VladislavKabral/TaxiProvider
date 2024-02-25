package by.modsen.taxiprovider.ridesservice.model.ride;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {

    private String lat;

    private String lon;
}
