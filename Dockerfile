# Stage 1: Build the Spring Boot application
FROM maven:3.8.7-jdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven pom.xml first to download dependencies
# This leverages Docker's layer caching. If pom.xml doesn't change,
# dependencies won't be re-downloaded on subsequent builds.
COPY pom.xml .

# Download dependencies (only if pom.xml changed)
RUN mvn dependency:go-offline

# Copy the rest of the application source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM openjdk:17-jdk-slim

# Expose the port your Spring Boot application listens on
EXPOSE 8080

# Copy the JAR file from the build stage
# Assuming your JAR is named 'your-application-name-0.0.1-SNAPSHOT.jar'
# You might need to adjust '*.jar' if you have multiple JARs or a specific name
COPY --from=build /app/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]