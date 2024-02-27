package by.modsen.taxiprovider.ridesservice.model.ride;

import by.modsen.taxiprovider.ridesservice.model.ride.address.Address;
import by.modsen.taxiprovider.ridesservice.model.ride.address.DestinationAddress;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @Min(value = 1, message = "Passenger's id must be a number and can't be less than one")
    private long passengerId;

    @Column(name = "driver_id")
    @Min(value = 1, message = "Driver's id must be a number and can't be less than one")
    private long driverId;

    @Column(name = "started_at")
    @JsonFormat(pattern = "yyyy-MM-dd, HH-mm-ss")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    @JsonFormat(pattern = "yyyy-MM-dd, HH-mm-ss")
    private LocalDateTime endedAt;

    @OneToOne
    @JoinColumn(name = "source_address_id", referencedColumnName = "id")
    @NotNull(message = "Source address must be not empty")
    private Address sourceAddress;

    @OneToMany(mappedBy = "ride")
    @NotNull(message = "Target address-(es) must be not empty")
    private List<DestinationAddress> destinationAddresses;

    @Column(name = "cost")
    private double cost;

    @Column(name = "status")
    private String status;
}
