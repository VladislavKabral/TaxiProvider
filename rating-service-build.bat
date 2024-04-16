cd RatingService
call mvn clean package -DskipTests
call docker build -t rating-service:0.0.1 .