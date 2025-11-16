FROM node:20 AS frontend
WORKDIR /frontend
COPY project/package*.json ./
RUN npm install
COPY project/ .
RUN npm run build

FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY manufacturing_system/pom.xml .
RUN mvn dependency:go-offline -B
COPY manufacturing_system/ .
COPY --from=frontend /frontend/dist ./src/main/resources/static
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-XX:+UseZGC","-XX:TieredStopAtLevel=1","-Dspring.main.lazy-initialization=true","-Dspring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false","-jar","app.jar"]
