package by.modsen.taxiprovider.ridesservice.model.ride;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "addresses")
@NamedEntityGraph(name = "address_entity_graph", attributeNodes = {@NamedAttributeNode("latitude"),
        @NamedAttributeNode("longitude")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "latitude")
    @NotNull(message = "Latitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong latitude format")
    private String latitude;

    @Column(name = "longitude")
    @NotNull(message = "Longitude must be not empty")
    @Pattern(regexp = "^-?\\d{1,3}\\.\\d{6,14}$", message = "Wrong longitude format")
    private String longitude;

    @OneToMany(mappedBy = "sourceAddress")
    private List<Ride> ride;

    @ManyToMany(mappedBy = "destinationAddresses")
    private List<Ride> rides;
}
