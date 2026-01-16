# Стадия сборки
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Стадия выполнения
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Создаем пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем собранный JAR
COPY --from=build /app/target/panayotov-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]