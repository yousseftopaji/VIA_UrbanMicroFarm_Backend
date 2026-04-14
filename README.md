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

- JDK 21 installed
- Internet access for first Maven Wrapper run (downloads Maven distribution)
- On Windows: use `mvnw.cmd` from PowerShell / CMD for best compatibility

## Build, Test, Run

### PowerShell (Windows recommended)

```powershell
cd "C:\Users\youss\OneDrive - ViaUC\VIA\DOC\tabloid\backend\UrbanMicroFarm_Backend"
.\mvnw.cmd clean verify
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
