FROM gradle:8.4-jdk17 AS build

WORKDIR /app

COPY ./gradle ./gradle
COPY ./gradlew .
COPY ./build.gradle .
COPY ./settings.gradle .

COPY src src

RUN ./gradlew shadowJar --no-daemon --stacktrace

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar ./app.jar

EXPOSE 8082
CMD ["java", "-jar", "app.jar"]
