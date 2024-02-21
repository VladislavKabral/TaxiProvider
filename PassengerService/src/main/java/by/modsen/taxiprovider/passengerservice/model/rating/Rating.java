package by.modsen.taxiprovider.passengerservice.model.rating;

import by.modsen.taxiprovider.passengerservice.model.passenger.Passenger;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating implements Comparable<Rating>{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "value")
    @NotNull(message = "Rating's value must be not empty")
    @Min(value = 1, message = "Minimal value of rating must be '1'")
    @Max(value = 5, message = "Maximum value of rating must be '5'")
    private int value;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "passenger_id", referencedColumnName = "id")
    private Passenger passenger;

    @Override
    public int compareTo(Rating rating) {
        return createdAt.compareTo(rating.getCreatedAt());
    }
}
