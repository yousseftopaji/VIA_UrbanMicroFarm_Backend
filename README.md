# Urban Micro Farming - Backend

Spring Boot backend for the Urban Micro Farming project.

## Tech Stack

- Java 21
- Spring Boot 4.0.5
- Maven Wrapper (`mvnw`, `mvnw.cmd`)
- Spring Data JPA
- Spring Security
- Spring Web MVC
- PostgreSQL (runtime)
- H2 (test profile)
- gRPC / Protobuf tooling
- Spring Integration MQTT

## Prerequisites

- JDK 21 installed (for local Maven runs)
- Docker Engine + Docker Compose plugin installed

## Build, Test, Run (local)

### Bash (Ubuntu)

```bash
cd /path/to/UrbanMicroFarm_Backend
chmod +x mvnw
./mvnw clean verify
./mvnw test
./mvnw spring-boot:run
```

## Docker (Ubuntu) - Backend + DB

Place these files in the project root (same level as `pom.xml`):

- `Dockerfile`
- `.dockerignore`
- `docker-compose.yml`
- `.env` (copy from `.env.example`)

### 1) Prepare environment file

```bash
cd /path/to/UrbanMicroFarm_Backend
cp .env.example .env
```

### 2) Build image and start containers

```bash
docker compose build
docker compose up -d
```

### 3) Check status/logs

```bash
docker compose ps
docker compose logs -f backend
docker compose logs -f db
```

### 4) Stop containers

```bash
docker compose down
```

### 5) Stop and delete DB volume (full reset)

```bash
docker compose down -v
```

## What gets created

- Image: `urbanmicrofarm-backend:dev` (from `Dockerfile`)
- Containers: `urbanmicrofarm-backend`, `urbanmicrofarm-db`
- Volume: `postgres_data` (persistent PostgreSQL data)
