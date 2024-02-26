package by.modsen.taxiprovider.ridesservice.model.ride.address;

import by.modsen.taxiprovider.ridesservice.model.ride.Ride;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destination_addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinationAddress {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "ride_id", referencedColumnName = "id")
    private Ride ride;
}
