# Step 1: Build frontend (Vue)
FROM node:20 AS frontend
WORKDIR /frontend
COPY project/ .
RUN npm install && npm run build

# Step 2: Build backend (Spring Boot)
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY manufacturing_system/ .

# 複製前端打包好的檔案到 Spring Boot 靜態資源路徑
COPY --from=frontend /frontend/dist ./src/main/resources/static

# 使用 Maven 打包，跳過測試
RUN mvn clean package -DskipTests

# Step 3: Create minimal runtime image
FROM openjdk:21-jdk-slim
WORKDIR /app

# 複製 JAR 檔到容器
COPY --from=build /app/target/*.jar app.jar

# 開放應用程式埠
EXPOSE 8080

# 啟動 Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]