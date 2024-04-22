# Tuum test assignment

This application is a RESTful API that simulates the core system of the bank.
---------------------------------------
## Prerequisites

Before you begin, ensure you have the following installed:

1. **SonarQube can be set-up running in a Docker container via 2nd repository.**
2. **Java JDK 17**: [Download JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
3. **Gradle**: [Installing Gradle](https://gradle.org/install/)
4. **Docker**: [Install Docker](https://docs.docker.com/get-docker/) (if you plan to use Docker containers), please consider to add docker compose functionality as well

---------------------------------------
### Build with Gradle

Local run and unit / integration test  with Gradle

```bash
docker-compose up -d --build postgres rabbitmq
````

NB! 
I haven`t implented the Spring profiles, so I kidnly as you to uncomment the following lines in application.yml file
Also, comment them back, if you want to build application with docker compose. They will wrap application into docker context

-  line 3:   url: ${JDBC_DATABASE_URL:jdbc:postgresql://localhost:5433/bank}
-  line 12:  host: localhost

```bash
./gradlew test
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

### How to test? 
Follow TUUM test assignment instructions to test the application.
Behaviour of the application reflect the requirements of the assignment.

PDF file embdded to application root folder.
PDF with the report, suggestion and scalability improvements is available in the root folder of the project.

---------------------------------------
### Access Swagger UI
Swagger UI is used to test the API.
You can access it via browser
http://localhost:8080/swagger-ui/index.html#/

DTO details described in Swagger UI schemas.

Application is accessible via: http://localhost:8080/swagger-ui/index.html
---------------------------------------
### Access RabbitMQ
RabbitMQ is used as message broker.
You can access it via browser
- Development profile: http://localhost:15672/ 
   credentials: user: user, password: password
---------------------------------------

### Test Brief
- 80% of the Account Service is covered with unit and integration tests. Confirmed with Sonar
- Some critical parts also covered with unit / integration tests, but mostly as repository tests, only reflecting understanding of the edge/positive/negative cases.

- Real test`s would be set-up with the help of the Test Containers, but due to the time constraints, I have not implemented them.
- Profiles also should be included, test could be inserted with csv / SQL files to emulate the real data and set-up profiles for the test environment.  
- Also, I would like to implement the test with the help of the WireMock, to emulate the external services and test the application in isolation.

## Optional
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