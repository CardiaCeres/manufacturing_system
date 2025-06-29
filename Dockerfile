# 使用輕量JDK映像
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 複製打包好的 Spring Boot jar
COPY manufacturing_system/target/*.jar app.jar

# 複製前端已編譯好的靜態檔案
COPY manufacturing_system/src/main/resources/static ./static

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]