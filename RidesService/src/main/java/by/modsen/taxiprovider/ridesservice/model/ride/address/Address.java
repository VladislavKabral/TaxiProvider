package by.modsen.taxiprovider.ridesservice.model.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import jakarta.persistence.*;
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
    private String lat;

    @Column(name = "lon")
    private String lon;

    @OneToOne(mappedBy = "sourceAddress")
    private Ride ride;

    @OneToMany(mappedBy = "address")
    private List<DestinationAddress> destinationAddresses;
}
