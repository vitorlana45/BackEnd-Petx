# Stage 1: Build the application
FROM ubuntu:24.04 AS build

LABEL authors="vitor lana"

# Install Java and Maven
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    maven

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Build the application
RUN mvn clean install

# Stage 2: Create the final image
FROM openjdk:21-jdk-slim

# Install Maven in the runtime image
RUN apt-get update && apt-get install -y maven

# Expose the application port
EXPOSE 8080

# Copy the JAR file from the build stage
COPY --from=build /app/target/backEndPetx-0.0.1-SNAPSHOT.jar /app/app.jar

# Set the working directory
WORKDIR /app

# Define the command to run the application with the "prod" profile
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dserver.port=${PORT}", "-jar", "app.jar"]
