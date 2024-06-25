
# Password Manager

[![](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=whit)]()
[![](https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E)]()
[![](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)]()
[![](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)]()
[![](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white)]()
[![](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)]()
[![](https://img.shields.io/badge/Material%20UI-007FFF?style=for-the-badge&logo=mui&logoColor=white)]()

Password manager contains of:
* React web application
* Spring backend application
* PostgreSQL database 

## Setup
Run docker-compose to create setup
```bash
docker-compose up
```

## App

### Frontend
Frontend is a React application that allows user to manage passwords. It is connected to backend application.

![Landing page](./readme/landing.png)
![Sign up](./readme/sign_up.png)
![Sign in](./readme/sign_in.png)
![Profile](./readme/profile.png)
![Manager](./readme/manager.png)
![Password](./readme/password.png)

### Backend
Backend is a Spring application that provides REST API for frontend application. It is connected to PostgreSQL database.

![Swagger](./readme/swagger.png)