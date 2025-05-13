# Use an official Java runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built by Maven/Gradle
COPY target/payment_service-0.0.1-SNAPSHOT.jar payment_service-0.0.1-SNAPSHOT.jar

# Expose the port your service runs on
EXPOSE 8083

# Command to run the application
ENTRYPOINT ["java", "-jar", "payment_service-0.0.1-SNAPSHOT.jar"]