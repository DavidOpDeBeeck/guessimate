FROM amazoncorretto:25-alpine

WORKDIR /app

COPY . .

RUN ./gradlew guessimate-api:bootJar --no-daemon

FROM amazoncorretto:25-alpine

COPY --from=0 app/guessimate-api/build/libs/guessimate-api-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]