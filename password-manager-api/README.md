# Password Manager API

[![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=whit)]()
[![](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)]()
[![](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white)]()
[![](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)]()

Password manager backend application and PostegreSQL database

## Swagger
Local SwaggerUI hosted on [link](http://localhost:8080/api/swagger-ui/index.html#/)

## Image
To build password-manager-api docker image execute
```bash
 mvn clean install docker:build
```

## Setup
Open password-manager-api directory
```bash
 cd password-manager-api
```

Run docker-compose to create setup
```bash
 docker-compose up
```

```yml
version: '3.7'
services:
  API:
    image: 'password-manager-api'
    build:
      context: .
    ports:
      - "8080:8080"
    depends_on:
      database:
          condition: service_healthy
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/password-manager-db
      - SPRING_DATASOURCE_USERNAME=app
      - SPRING_DATASOURCE_PASSWORD=password
      - SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop

  database:
    image: postgres:latest
    ports:
      - "5432:5432"
    environment:
      POSTGRES_USER: app
      POSTGRES_PASSWORD: password
      POSTGRES_DB: password-manager-db
    healthcheck:
      test: pg_isready -U app

```




