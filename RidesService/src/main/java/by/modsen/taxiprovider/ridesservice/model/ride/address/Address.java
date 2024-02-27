package by.modsen.taxiprovider.ridesservice.model.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lat")
    @NotNull(message = "Latitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong latitude format")
    private String lat;

    @Column(name = "lon")
    @NotNull(message = "Longitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong longitude format")
    private String lon;

    @OneToOne(mappedBy = "sourceAddress")
    private Ride ride;

    @OneToMany(mappedBy = "address")
    private List<DestinationAddress> destinationAddresses;
}
