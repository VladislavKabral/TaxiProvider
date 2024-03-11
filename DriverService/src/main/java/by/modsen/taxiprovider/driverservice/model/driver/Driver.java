package by.modsen.taxiprovider.driverservice.model.driver;

import by.modsen.taxiprovider.driverservice.model.rating.Rating;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "drivers")
@NamedEntityGraph(name = "driver-entity-graph", attributeNodes = @NamedAttributeNode("ratings"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "ride_status")
    private String rideStatus;

    @OneToMany(mappedBy = "driver")
    private List<Rating> ratings;
}
