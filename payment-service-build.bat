cd PaymentService
call mvn clean package -DskipTests
call docker build -t payment-service:0.0.1 .