package by.modsen.taxiprovider.passengerservice.model.passenger;

import by.modsen.taxiprovider.passengerservice.model.card.CreditCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "passengers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lastname")
    @NotEmpty(message = "Passenger's lastname must be not empty")
    @Size(min = 2, max = 50, message = "Passenger's lastname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Passenger's lastname must contain only letters")
    private String lastname;

    @Column(name = "firstname")
    @NotEmpty(message = "Passenger's firstname must be not empty")
    @Size(min = 2, max = 50, message = "Passenger's firstname must be between 2 and 50 symbols")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Passenger's firstname must contain only letters")
    private String firstname;

    @Column(name = "email")
    @NotEmpty(message = "Passenger's email must be not empty")
    @Email(message = "Wrong email format")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Passenger's password must be not empty")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number must be not empty")
    @Pattern(regexp = "^(\\+375|80)(29|25|44|33)(\\d{3})(\\d{2})(\\d{2})$", message = "Wrong phone number format")
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id")
    private CreditCard creditCard;

    @Column(name = "status")
    private String status;
}
