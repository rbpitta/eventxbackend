# Stage 1: Build the Spring Boot application using Maven with Temurin 17
FROM maven:3.9.11-eclipse-temurin-17 AS build # Ou 3.9-eclipse-temurin-17 ou 3-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image using OpenJDK 17
FROM openjdk:17-slim-bullseye # Ou openjdk:17-bullseye, ou eclipse-temurin:17-jre-alpine (ainda menor)

EXPOSE 8080

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]