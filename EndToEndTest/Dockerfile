FROM openjdk:17

WORKDIR /application

COPY target/EndToEndTest-0.0.1-SNAPSHOT.jar end-to-end-tests-service.jar

ENTRYPOINT ["java", "-jar", "end-to-end-tests-service.jar"]