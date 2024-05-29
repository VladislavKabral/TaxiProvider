cd DriverService
call mvn clean package -DskipTests
mkdir target\extracted
call java -Djarmode=layertools -jar target\DriverService-0.0.1-SNAPSHOT.jar extract --destination target\extracted
call docker build -t driver-service:0.0.1 .