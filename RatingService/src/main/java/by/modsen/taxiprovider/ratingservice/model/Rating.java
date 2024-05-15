package by.modsen.taxiprovider.ratingservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "ratings_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating implements Comparable<Rating> {

    @Id
    private String id;

    private long taxiUserId;

    private String role;

    private int value;

    private LocalDateTime createdAt;

    @Override
    public int compareTo(Rating rating) {
        return createdAt.compareTo(rating.getCreatedAt());
    }
}
