package by.modsen.taxiprovider.ridesservice.model.ride;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rides")
@NamedEntityGraph(name = "ride_entity_graph", attributeNodes = {@NamedAttributeNode("passengerId"),
        @NamedAttributeNode("driverId")})
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

    @ManyToOne
    @JoinColumn(name = "source_address_id", referencedColumnName = "id")
    @NotNull(message = "Source address must be not empty")
    private Address sourceAddress;

    @ManyToMany
    @JoinTable(
            name = "ride_to_address",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    @NotNull(message = "Target address-(es) must be not empty")
    @Size(min = 1, message = "Must be at least one destination address")
    private List<Address> destinationAddresses;

    @Column(name = "cost")
    private double cost;

    @Column(name = "status")
    private String status;
}
