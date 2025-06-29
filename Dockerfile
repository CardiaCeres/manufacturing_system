# 輕量 JRE 映像
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 只複製已打包的 Spring Boot jar（裡面包含前端靜態資源）
COPY manufacturing_system/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]