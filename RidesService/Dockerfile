FROM openjdk:17

WORKDIR /application

COPY target/RidesService-0.0.1-SNAPSHOT.jar rides-service.jar

ENTRYPOINT ["java", "-jar", "rides-service.jar"]