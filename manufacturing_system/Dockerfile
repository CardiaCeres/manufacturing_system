# 使用 OpenJDK 21 作為基礎映像
FROM openjdk:21-jdk-slim

# 設定工作目錄
WORKDIR /app

# 複製 Spring Boot 的 JAR 檔案（已經包含 Vue 靜態檔）
COPY target/manufacturing_system-0.0.1-SNAPSHOT.jar app.jar

# 啟動應用程式
ENTRYPOINT ["java", "-jar", "app.jar"]