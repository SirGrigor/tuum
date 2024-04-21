FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean build -x test --no-daemon

RUN cp build/libs/tuum-0.0.1-SNAPSHOT.jar app.jar

RUN rm -rf ./build ./src ./gradlew ./gradle

ENTRYPOINT ["java", "-jar", "app.jar"]
