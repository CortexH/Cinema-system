# DOCKERFILE ticket service

FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml ./pom.xml

COPY cinema-contracts/pom.xml ./cinema-contracts/pom.xml
COPY ticket-service/pom.xml ./ticket-service/pom.xml
COPY movie-service/pom.xml ./movie-service/pom.xml
COPY room-service/pom.xml ./room-service/pom.xml

RUN mvn -f ./pom.xml dependency:go-offline -B

COPY cinema-contracts/src ./cinema-contracts/src
COPY ticket-service/src ./ticket-service/src

RUN mvn -f ./pom.xml clean package -pl :ticket-service -am -B

FROM eclipse-temurin:23-jre-alpine

WORKDIR /app

ENV KAFKA_BOOTSTRAP_SERVERS="kafka:9093"
ENV SCHEMA_REGISTRY_URL="http://schema-registry:8081"
ENV ROOM_SERVICE_URL="http://room-service:6969"

COPY --from=builder /app/ticket-service/target/ticket-service-1.0.0-SNAPSHOT.jar app.jar

EXPOSE 6968

ENTRYPOINT ["java", "-jar", "app.jar"]


