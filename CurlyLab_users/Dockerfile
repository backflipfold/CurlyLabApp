FROM gradle:8.4-jdk17 AS build

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY src src

RUN gradle clean shadowJar --no-daemon --stacktrace

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar ./app.jar

EXPOSE 8081
CMD ["java", "-jar", "app.jar"]
