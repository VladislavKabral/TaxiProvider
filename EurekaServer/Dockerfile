FROM openjdk:17

WORKDIR /application

COPY target/EurekaServer-0.0.1-SNAPSHOT.jar eureka-server.jar

ENTRYPOINT ["java", "-jar", "eureka-server.jar"]