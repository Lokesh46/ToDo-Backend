# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven project files first to leverage Docker cache
COPY pom.xml .
COPY src ./src

# Build the application (includes clean, test, and package)
RUN mvn clean install

# Stage 2: Create a minimal runtime image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
