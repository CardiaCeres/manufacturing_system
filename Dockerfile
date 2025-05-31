# Step 1: Build frontend
FROM node:20 AS frontend
WORKDIR /frontend
COPY project/ .
RUN npm install && npm run build

# Step 2: Build backend
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY manufacturing_system/ .
COPY --from=frontend /frontend/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

# Step 3: Run Spring Boot
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/manufacturing_system-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]