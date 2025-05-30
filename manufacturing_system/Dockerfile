# 第一階段：用 Maven 建置 JAR
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 第二階段：執行 JAR
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/manufacturing_system-0.0.1-SNAPSHOT.jar app.jar


# 告訴 Docker / Render 這個應用會監聽 8080 port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]