cd ApiGateway
call mvn clean package -DskipTests
mkdir target\extracted
call java -Djarmode=layertools -jar target\ApiGateway-0.0.1-SNAPSHOT.jar extract --destination target\extracted
call docker build -t api-gateway:0.0.1 .