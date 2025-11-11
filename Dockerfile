# =======================
# Step 1: Build frontend (Vue)
# =======================
FROM node:20-alpine AS frontend
WORKDIR /frontend
COPY project/package*.json ./
RUN npm install --omit=dev
COPY project/ .
RUN npm run build

# =======================
# Step 2: Build backend (Spring Boot)
# =======================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# 先複製 pom.xml 並預先下載 dependencies（利用快取）
COPY manufacturing_system/pom.xml .
RUN mvn dependency:go-offline -B

# 再複製完整專案
COPY manufacturing_system/ .

# 複製前端靜態檔案進入 Spring Boot 資源目錄
COPY --from=frontend /frontend/dist ./src/main/resources/static

# 建置 Spring Boot Jar
RUN mvn clean package -DskipTests

# =======================
# Step 3: Runtime image
# =======================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 複製 JAR
COPY --from=builder /app/target/*.jar app.jar

# 開放 port
EXPOSE 8080

# 優化啟動參數：減少啟動時間與記憶體消耗
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
