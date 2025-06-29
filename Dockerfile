FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 複製打包好的 JAR（前一步會統一命名為 app.jar）
COPY app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]