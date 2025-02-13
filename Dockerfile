FROM openjdk:17-jdk-slim

WORKDIR /app

COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY build.gradle.kts .
COPY settings.gradle.kts .

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon  # 의존성 미리 다운로드

COPY . .
RUN ./gradlew bootJar  # Spring Boot JAR 빌드

CMD ["java", "-jar", "build/libs/moin-challenge-0.0.1-SNAPSHOT.jar"]
