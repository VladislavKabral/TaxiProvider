package by.modsen.taxiprovider.ridesservice.model.ride;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.math.BigDecimal;
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

    @ManyToOne()
    @JoinColumn(name = "source_address_id", referencedColumnName = "id")
    private Address sourceAddress;

    @ManyToMany()
    @BatchSize(size = 150)
    @JoinTable(
            name = "ride_to_address",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> destinationAddresses;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "status")
    private String status;
}
