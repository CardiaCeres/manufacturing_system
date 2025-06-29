# 使用 OpenJDK 作為 base image
FROM openjdk:21-jdk-slim

# 設定工作目錄
WORKDIR /app

# 複製 Spring Boot JAR（注意請根據實際 JAR 名稱調整）
COPY manufacturing_system/target/*.jar app.jar

# 開放應用埠
EXPOSE 8080

# 啟動 Spring Boot 應用
ENTRYPOINT ["java", "-jar", "app.jar"]