FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY manufacturing_system/target/app.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]