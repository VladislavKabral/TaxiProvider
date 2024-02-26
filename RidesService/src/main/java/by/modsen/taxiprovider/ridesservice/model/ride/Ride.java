package by.modsen.taxiprovider.ridesservice.model.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import by.modsen.taxiprovider.ridesservice.model.ride.address.DestinationAddress;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rides")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ride {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "passenger_id")
    private long passengerId;

    @Column(name = "driver_id")
    private long driverId;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @OneToOne
    @JoinColumn(name = "source_address_id", referencedColumnName = "id")
    private Address sourceAddress;

    @OneToMany(mappedBy = "ride")
    private List<DestinationAddress> destinationAddresses;

    @Column(name = "cost")
    private double cost;
}
