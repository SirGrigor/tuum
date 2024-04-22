# Base image
FROM openjdk:17-jdk-slim as base

WORKDIR /app
COPY . .

RUN apt-get update && apt-get install -y netcat

RUN chmod +x ./gradlew

FROM base as build-dev
RUN ./gradlew clean build -x test --no-daemon

FROM openjdk:17-jdk-slim as application
COPY --from=build-dev /app/build/libs/*.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM base as build-test
COPY wait-for-it.sh ./wait-for-it.sh
RUN chmod +x ./wait-for-it.sh
CMD ["./wait-for-it.sh", "postgres-test:5432", "--", "./gradlew", "test", "--no-daemon"]
