# Stage 1: Maven और Java 21 से प्रोजेक्ट को बिल्ड (Build) करें
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: हल्की-फुल्की Java 21 Alpine इमेज में ऐप को रन (Run) करें
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]