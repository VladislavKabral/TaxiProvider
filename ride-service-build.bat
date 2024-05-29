cd RidesService
call mvn clean package -DskipTests
mkdir target\extracted
call java -Djarmode=layertools -jar target\RidesService-0.0.1-SNAPSHOT.jar extract --destination target\extracted
call docker build -t ride-service:0.0.1 .