FROM amazoncorretto:25-alpine AS build

WORKDIR /workspace

COPY gradlew settings.gradle.kts build.gradle.kts ./
COPY gradle ./gradle
COPY guessimate-api/build.gradle.kts ./guessimate-api/
COPY guessimate-lobby/api/build.gradle.kts ./guessimate-lobby/api/
COPY guessimate-lobby/application/build.gradle.kts ./guessimate-lobby/application/
COPY guessimate-session/api/build.gradle.kts ./guessimate-session/api/
COPY guessimate-session/application/build.gradle.kts ./guessimate-session/application/

RUN ./gradlew :guessimate-api:dependencies --no-daemon --quiet

COPY guessimate-api/src ./guessimate-api/src
COPY guessimate-lobby/api/src ./guessimate-lobby/api/src
COPY guessimate-lobby/application/src ./guessimate-lobby/application/src
COPY guessimate-session/api/src ./guessimate-session/api/src
COPY guessimate-session/application/src ./guessimate-session/application/src

RUN ./gradlew :guessimate-api:bootJar --no-daemon

FROM amazoncorretto:25-alpine AS runtime

RUN apk add --no-cache wget \
    && addgroup -S guessimate \
    && adduser -S guessimate -G guessimate

WORKDIR /app

COPY --from=build --chown=guessimate:guessimate /workspace/guessimate-api/build/libs/guessimate-api-1.0.jar ./app.jar

USER guessimate

EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
