FROM gradle:8.5-jdk17 AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .

COPY . .
RUN ./gradlew build --no-daemon

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/moin-challenge-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 8080
