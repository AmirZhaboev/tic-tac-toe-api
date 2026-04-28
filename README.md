# Tic Tac Toe API

REST API для игры в крестики-нолики с JWT-аутентификацией, хранением игровых сессий и leaderboard системой.

Проект разработан на Spring Boot с использованием многослойной архитектуры: Web → Domain → Data Layer.

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- JWT Authentication
- Spring Data JPA
- Hibernate
- Gradle
- REST API
- SQL Database
- HTML / JavaScript (simple frontend)

---

## Features

### Authentication

- регистрация пользователей
- login
- JWT access token
- refresh token
- авторизация запросов

Endpoints:

- `POST /auth/signup`
- `POST /auth/login`
- `POST /auth/refresh`

---

## Game Management

Пользователь может:

- создать новую игру
- делать ходы
- получать текущее состояние игры
- завершать игровые сессии

Endpoints:

- `POST /games`
- `POST /games/{id}/move`
- `GET /games/{id}`

---

## Game Logic

Реализована игровая логика:

- проверка валидности хода
- определение победителя
- проверка ничьей
- обновление состояния поля
- завершение игры

---

## Leaderboard

Система статистики игроков:

- win ratio
- leaderboard
- игровая статистика

---

## Architecture

### Web Layer

- Controllers
- DTO
- request/response models

```bash
web/
├── controller
├── mapper
└── model
```

---

### Domain Layer

- business logic
- game services
- auth services

```bash
domain/
├── model
└── service
```

---

### Data Layer

- JPA entities
- repositories
- database mappers

```bash
datasource/
├── model
├── repository
├── mapper
└── projection
```

---

## Security

Реализовано через :contentReference[oaicite:0]{index=0} и JWT:

- AuthFilter
- JwtProvider
- SecurityConfig
- protected endpoints

---

## Project Structure

```bash
src/main/java/tictactoe/
├── datasource
├── domain
├── security
├── web
└── TicTacToeApplication.java
```

---

## Run Locally

Клонировать репозиторий:

```bash
git clone https://github.com/your_username/tic-tac-toe-api.git
cd tic-tac-toe-api
```

Запуск приложения:

```bash
./gradlew bootRun
```

Или:

```bash
gradle build
java -jar build/libs/*.jar
```

---

## What I Practiced

В этом проекте практиковал:

- backend development
- REST API design
- Spring Security
- JWT authentication
- layered architecture
- database design
- game state management
- business logic implementation
