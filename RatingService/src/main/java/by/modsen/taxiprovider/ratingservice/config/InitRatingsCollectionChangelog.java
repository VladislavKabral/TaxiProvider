package by.modsen.taxiprovider.ratingservice.config;

import by.modsen.taxiprovider.ratingservice.model.Rating;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.schema.JsonSchemaProperty;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;
import org.springframework.data.mongodb.core.validation.Validator;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ChangeUnit(id = "init-ratings-collection", order = "001", author = "Vladislav-Kabral")
@RequiredArgsConstructor
public class InitRatingsCollectionChangelog {

    private final MongoTemplate mongoTemplate;

    private static final int GRADES_COUNT = 30;

    private static final int USERS_COUNT = 3;

    private static final String PASSENGER_ROLE_NAME = "PASSENGER";

    private static final String DRIVER_ROLE_NAME = "DRIVER";

    private static final int DEFAULT_GRADE_VALUE = 5;

    @BeforeExecution
    public void beforeExecution(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection("ratings_collection", CollectionOptions.empty()
                .validator(Validator.schema(MongoJsonSchema.builder()
                        .properties(
                                JsonSchemaProperty.objectId("id"),
                                JsonSchemaProperty.int64("taxiUserId"),
                                JsonSchemaProperty.string("role"),
                                JsonSchemaProperty.int32("value"),
                                JsonSchemaProperty.date("createdAt"))
                        .build())));
    }

    @RollbackBeforeExecution
    public void rollbackBefore() {
        mongoTemplate.dropCollection("ratings_collection");
    }

    @Execution
    public void changeSet() {
        for (int i = 1; i <= USERS_COUNT; i++) {
            for (int j = 0; j < GRADES_COUNT; j++) {
                Rating rating = Rating.builder()
                        .taxiUserId(i)
                        .role(PASSENGER_ROLE_NAME)
                        .value(DEFAULT_GRADE_VALUE)
                        .createdAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build();

                mongoTemplate.save(rating, "ratings_collection");

                rating = Rating.builder()
                        .taxiUserId(i)
                        .role(DRIVER_ROLE_NAME)
                        .value(DEFAULT_GRADE_VALUE)
                        .createdAt(ZonedDateTime.now(ZoneId.of("UTC")).toLocalDateTime())
                        .build();

                mongoTemplate.save(rating, "ratings_collection");
            }
        }

    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.dropCollection("ratings_collection");
    }
}
