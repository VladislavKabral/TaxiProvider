package by.modsen.taxiprovider.ridesservice.model.promocode;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promo_codes")
@NamedEntityGraph(name = "promoCode_entity_graph", attributeNodes = @NamedAttributeNode("value"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCode {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value")
    @Size(min = 4, max = 50, message = "Promo code must be between 4 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Promo code must contains only letters and numbers")
    private String value;

    @Column(name = "discount")
    private double discount;
}
