FROM --platform=$BUILDPLATFORM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

COPY build.gradle.kts settings.gradle.kts ./
RUN ./gradlew dependencies --no-daemon

COPY src src
RUN ./gradlew bootJar --no-daemon

FROM --platform=$TARGETPLATFORM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75","-jar","app.jar"]