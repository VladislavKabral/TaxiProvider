# Modsen Taxi provider

This is project represents an API for a taxi aggregator. There are some features for passengers and drivers. That API 
contains two types of roles: 'USER' and 'ADMIN' ('ADMIN' has an access for all the features). This project uses some 
external services, for example, Stripe for payment transactions and 2GIS for calculation of rides' routes. 

# Project features

There are main features which the API provides.

+ Registration;
+ Authentication and authorization;
+ Getting all passengers;
+ Getting all drivers;
+ Editing of passenger/driver profile;
+ Booking of rides by specifying pick-up address and destination address (or several);
+ Rating passengers/driver;
+ View passenger/driver rating;
+ View the estimated cost of a ride;
+ View rides history;
+ View passenger/driver profile;
+ Payment with cash or with a bank card;
+ Entering promo codes;
+ Transfer of money to a bank card.

# Project technologies

There are the main technologies used in the development.

+ **Spring Framework, Boot** as a main framework;
+ **Spring Cloud: Eureka, Gateway** as a service registry and an api gateway provider;
+ **PostgreSQL** as a DBMS;
+ **Keycloak** as a provider of OAuth 2.0;
+ **MongoDB** as a non-relational database;
+ **Spring Data, Hibernate** as an ORM;
+ **Apache Kafka** as a message broker;
+ **ELK Stack** as tools for logging and visualization;
+ **Grafana, Prometheus** as tools for metrics and visualization;
+ **JUnit, Mockito, RestAssured, WireMock, Cucumber** as tools for testing;
+ **Docker** as a platform for containerization.

