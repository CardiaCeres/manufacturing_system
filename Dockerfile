# ================================
# Step 1: Build frontend (Vue)
# ================================
FROM node:20 AS frontend
WORKDIR /frontend
COPY project/package*.json ./
RUN npm install
COPY project/ .
RUN npm run build


# ================================
# Step 2: Build backend (Spring Boot)
# ================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# 預先快取 dependency
COPY manufacturing_system/pom.xml .
RUN mvn dependency:go-offline -B

# 複製完整 backend 專案
COPY manufacturing_system/ .

# 複製前端檔案到 Spring Boot 靜態目錄
COPY --from=frontend /frontend/dist ./src/main/resources/static

# 編譯成 fat jar
RUN mvn clean package -DskipTests


# ================================
# Step 3: Runtime image — DISTROLESS + JDK21
# ================================
FROM gcr.io/distroless/java21-debian11 AS runtime

WORKDIR /app

# 複製 jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# 使用最快 JVM 啟動參數
ENTRYPOINT [
  "java",
  "-XX:+UseContainerSupport",
  "-XX:MaxRAMPercentage=75.0",

  "-XX:+UseZGC",
  "-XX:+TieredStopAtLevel=1",                      # 最快 tiered compilation
  "-Dspring.main.lazy-initialization=true",        # 加速啟動
  "-Dspring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults=false",  # 減少 Hibernate 啟動時間
  "-jar",
  "app.jar"
]
