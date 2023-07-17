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

## Run with docker
Build password-manager-api image
```bash
mvn clean install docker:build
```
Open password-manager-api directory
```bash
cd password-manager-api
```

Run docker-compose to create setup
```bash
docker-compose up
```