package by.modsen.taxiprovider.ratingservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating implements Comparable<Rating> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "taxi_user_id")
    private long taxiUserId;

    @Column(name = "role")
    private String role;

    @Column(name = "value")
    private int value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public int compareTo(Rating rating) {
        return createdAt.compareTo(rating.getCreatedAt());
    }
}
