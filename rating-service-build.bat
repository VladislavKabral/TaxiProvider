cd RatingService
call mvn clean package -DskipTests
mkdir target\extracted
call java -Djarmode=layertools -jar target\RatingService-0.0.1-SNAPSHOT.jar extract --destination target\extracted
call docker build -t rating-service:0.0.1 .