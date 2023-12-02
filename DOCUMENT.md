# Assignment - Recipe Manager
Spring boot application to manage Recipe Manager. Application used to create new recipe, update existing recipe, delete and search based on user criteria.

## Contents

* [Prerequisites](#prerequisites)
* [Tech Stack](#tech-stack)
* [Spring Dependencies](#spring-dependencies)
* [Arch Flow](#arch-flow)
* [System Design](#system-design)
    * [Controller Layer](#controller-layer)
    * [Service Layer](#service-layer)
    * [Data Access Layer](#data-access-layer)
    * [Security](#security)
* [API End Points](#API-end-points)
* [Swagger Endpoint](#swagger-endpoint)
* [Maven Build Command](#maven-build-command)
* [Deployment](#deployment)
    * [Running Locally](#running-locally)
    * [Running in Docker](#running-in-docker)
* [Notes](#notes)

### Prerequisites:
1. Docker and Docker Compose plugin
2. IDE - IntelliJ, Eclipse, User preferred
3. PostgreSQL

### Tech Stack:
1. Spring Boot - 3.2.0
2. Java - 17
3. Maven
4. Docker
5. Docker Compose
6. PostgreSQL

## Spring Dependencies:
1. Spring Web - For handling rest calls
2. Spring security - To authenticate user request
3. Spring Data JPA - To access database using Spring Data API
4. Spring Validation - To validate the form object
5. Spring OAuth2 Resource Server - To register the user in auth server 
3. Spring Swagger (OpenAPI) - To generate documentations
4. Prometheus - To collect application metrics
5. Testcontainers - For JPA Repository and for integration test

### Arch Flow:
![arch_flow.png](arch_flow.png)

### System Design:
The Rest application is microservice based layered architectured RESTful Web Service. This service can be deployed independently on premise / cloud / containers. Application has below layers,  

## Controller Layer:
1. Responsible for handling incoming request
2. The endpoints are secured using JWT

## Service Layer:
1. Contains business logic
2. Get data from controller, implement business logic by interacting with data access layer

## Data Access Layer:
1. Interact with configured database
2. Application uses DATA JPA Api  - JPQL and Specification to handle complex criteria

### Security:
1. Each request is authorized except swagger, management endpoints.
2. Before send data to any endpoint, user should get JWT token by accessing endpoint token
3. Unauthorized users will get access denied exceptions.

### API End Points
Application uses below endpoints for CRUD operations,
**Note: With all given below api end points request, make sure to include header `Content-Type as application/json`**
API End Point | Method | Purpose | Request | Response
------------ | ------------- | ------------- | ------------ | -------------
/api/v1/recipe/token | POST | Authenticate and get JWT Token | User Model with user name and password | JWT Token on Success, 403 Forbidden on failure
/api/v1/recipe | POST | Create a new recipe | RecipeForm Model and valid JWT Token as bearer token as auth header| Recipe Model with 201 Created on Success, 400 Bad request on failure
/api/v1/recipe/{id} | GET | Get an existing recipe | Recipe id as path parameter and valid JWT Token as bearer token as auth header | Recipe Model with 200 OK on Success with Recipe data, 401 Not Found on failure
/api/v1/recipe | GET | Load all recipes as list | Valid JWT Token as bearer token as auth header | Recipes as list with 200 OK on success, 401 Not Found on failure
/api/v1/recipe/{id} | PUT | Update an existing recipe | Updated Recipe Model and valid JWT Token as bearer token as auth header | Recipe Model with 200 OK on Success, 401 Not Found on failure
/api/v1/recipe/{id} | DELETE | Delete an existing recipe | Recipe id as path parameter and valid JWT Token as bearer token as auth header | Deletion message with 200 OK on success, 401 Not Found on failure

### Swagger Endpoint
* Swagger URL - http://localhost:8080/swagger-ui/index.html

### Application Configuration
Application uses spring profile, to run application in different env please configure the profile value in starting command. Spring profile gives flexibility to configure different sources for different envirnoment.
```
    java -jar target/Recipes-Service-1.0.jar --spring.profiles.active=docker
```

### Maven Build Command
Run below maven command to generate jars and push it to a nexus repo,
Clean Install in debug mode - `mvn clean install -U -X`
Skip Test Cases - `mvn clean install -DskipTests=true`

## Deployment:
We can deploy and run applications locally and also in docker.

### Running Locally
To run applications locally please follow below steps,

1. Run the postgres db servers by using docker compose command
```
docker compose -f docker-servers.yml up
```
2. Use IDE or spring boot run command to run application locally, use application.yml

### Running In Docker
1. To run applications inside docker use below command.
```
docker compose -f docker-compose.yml up
docker compose -f docker-compose.yml down #To Stop docker containers
```

2. Application uses spring boot profile docker (application-docker.yaml). The docker profile uses docker network connectivity ex: to connect with postgres we need to use db:9092 as bootstrap servers URL.

## Manual Testing using postman or curl
Please check curl-commands.md

### Notes:
1. Unit test and Integration test are not written 100%

### Useful Commands:
1. Run docker compose
```
docker compose -f docker-servers.yml up
```

2. Stop docker compose
```
docker compose -f docker-servers.yml down
```

3. Check docker container
```
docker ps
```

3. Kill docker container
```
docker kill <<CONTAINER_ID>>
```