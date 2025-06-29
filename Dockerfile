# 使用輕量版 OpenJDK 21 JRE 映像
FROM eclipse-temurin:21-jre-alpine

# 設定工作目錄
WORKDIR /app

# 複製已編譯好的 Spring Boot Jar (請確認 jar 名稱正確)
COPY manufacturing_system/target/app.jar ./app.jar

# 開放 8080 埠口
EXPOSE 8080

# 啟動 Spring Boot 應用
ENTRYPOINT ["java", "-jar", "app.jar"]