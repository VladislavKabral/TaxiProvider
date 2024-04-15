FROM openjdk:17

WORKDIR /application

COPY target/RatingService-0.0.1-SNAPSHOT.jar rating-service.jar

ENTRYPOINT ["java", "-jar", "rating-service.jar"]