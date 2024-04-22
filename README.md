# Tuum test assignment

This application is a RESTful API that simulates the core system of the bank.
---------------------------------------
## Prerequisites

Before you begin, ensure you have the following installed:
If your prefer running application with docker first 3 steps can be skipped.

1. **SonarQube can be set-up running in a Docker container via 2nd repository.**
2. **Java JDK 17**: [Download JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
3. **Gradle**: [Installing Gradle](https://gradle.org/install/)
4. **Docker**: [Install Docker](https://docs.docker.com/get-docker/) (if you plan to use Docker containers), please consider to add docker compose functionality as well

---------------------------------------
### Build with Gradle

For testing applicatiio
---------------------------------------
NB! Switch set-up in application properties for DB and RabbitMQ url. By Default enable Docker build
```bash
java -jar build/libs/tuum-0.0.1-SNAPSHOT.jar
```

---------------------------------------
### Build with Docker
1. Build project with docker
Start Dev
```bash
docker-compose -f docker-compose.yml up --build
```

Stop Dev
```bash
docker-compose -f docker-compose.dev.yml down
````


Start Test
```bash
docker-compose -f docker-compose.test.yml up --build
```

Stop test
```bash
docker-compose -f docker-compose.test.yml up down
````
Application is accessible via: http://localhost:8080/swagger-ui/index.html
---------------------------------------
### Access RabbitMQ
RabbitMQ is used as message broker.
You can access it via browser
- Development profile: http://localhost:15672/ 
   credentials: user: user, password: password
---------------------------------------
### SonarQube Integration
If you would like to check the quality of your code, you can integrate the project with SonarQube.
Application has all required plugins and configurations to run SonarQube analysis.

For SonarQube integration, follow the steps below:
Clone separate repository for SonarQube server and run it locally:

https://github.com/SirGrigor/sonar

Apply your own rules. Once project is set up, generate a token and use it in the following command:

```bash
./gradlew sonar -D"sonar.projectKey=ilgrig" -D"sonar.projectName=ilgrig" -D"sonar.host.url=http://localhost:9000" -D"sonar.login=sqp_d7f25edff69849098c56634173d67ca4c3764d50"

```