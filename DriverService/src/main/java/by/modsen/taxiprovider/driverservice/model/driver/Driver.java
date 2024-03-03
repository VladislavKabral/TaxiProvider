package by.modsen.taxiprovider.driverservice.model.driver;

import by.modsen.taxiprovider.driverservice.model.rating.Rating;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "driver")
    private List<Rating> ratings;
}
