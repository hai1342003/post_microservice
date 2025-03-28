# Use an official Java runtime as a base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file built by Maven/Gradle
COPY target/order_service-0.0.1-SNAPSHOT.jar order_service-0.0.1-SNAPSHOT.jar

# Expose the port your service runs on
EXPOSE 8084

# Command to run the application
ENTRYPOINT ["java", "-jar", "order_service-0.0.1-SNAPSHOT.jar"]
