#!/bin/sh

echo "Waiting for PostgreSQL to be ready..."

# Lặp đến khi database phản hồi kết nối
until pg_isready -h database -p 5432 > /dev/null 2> /dev/null; do
  echo "Waiting for postgres..."
  sleep 2
done

echo "Postgres is up - starting the app."

# Chạy Spring Boot
exec java -jar delivery_service-0.0.1-SNAPSHOT.jar
