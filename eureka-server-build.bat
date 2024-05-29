cd EurekaServer
call mvn clean package -DskipTests
mkdir target\extracted
call java -Djarmode=layertools -jar target\EurekaServer-0.0.1-SNAPSHOT.jar extract --destination target\extracted
call docker build -t eureka-server:0.0.1 .