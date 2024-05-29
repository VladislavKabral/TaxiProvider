cd PassengerService
call mvn clean package -DskipTests
mkdir target\extracted
call java -Djarmode=layertools -jar target\PassengerService-0.0.1-SNAPSHOT.jar extract --destination target\extracted
call docker build -t passenger-service:0.0.1 .