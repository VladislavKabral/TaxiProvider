package by.modsen.taxiprovider.passengerservice.model.card;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credit_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number")
    @Pattern(regexp = "[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}", message = "Wrong account number format")
    @NotEmpty
    private String number;

    @Column(name = "value")
    @Min(value = 0, message = "Account's value can't be negative")
    private long value;

    @OneToOne(mappedBy = "creditCard")
    private Passenger passenger;
}
