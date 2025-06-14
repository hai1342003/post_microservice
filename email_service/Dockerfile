# Use official Java image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR and wait script
COPY target/delivery_service-0.0.1-SNAPSHOT.jar delivery_service-0.0.1-SNAPSHOT.jar
COPY wait-for-postgres.sh wait-for-postgres.sh

RUN apt-get update && apt-get install -y postgresql-client
# Chmod script
RUN chmod +x wait-for-postgres.sh

EXPOSE 8081

# Use the wait script as entrypoint
ENTRYPOINT ["./wait-for-postgres.sh"]
