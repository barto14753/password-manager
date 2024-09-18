<div align="center">
  <h1 align="center">Password Manager</h1>
</div>
<p align="center">
    <a><img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=whit"></a>
    <a><img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB"></a>
    <a><img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white"></a>
    <a><img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white"></a>
</p>

<p align="center">
    <a href="https://github.com/barto14753/password-manager/actions/workflows/api-ci.yml"><img src="https://github.com/barto14753/script-manager/actions/workflows/api-ci.yml/badge.svg" alt="API-CI"></a>
    <a href="https://github.com/barto14753/password-manager/actions/workflows/frontend-ci.yml"><img src="https://github.com/barto14753/script-manager/actions/workflows/frontend-ci.yml/badge.svg" alt="FRONTEND-CI"></a>
</p>

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