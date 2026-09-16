# Service Foo Architecture Overview

This document provides an overview of the architecture for the Service Foo application, including its main components, design principles, deployment model, and interactions with other services.

## Service Definition

Service Foo is a reference self-contained service within the application ecosystem.

It serves as a reference implementation for other services and demonstrates a representative set of common backend and infrastructure capabilities, including:

- Java version (e.g. Java 17)
- Spring Boot
- Maven
- REST API
- Testing
- Checkstyle
- SpotBugs
- CI
- Logging
- Configuration 
- Containerization (Docker)
- Application/build versioning (Maven)
- Database persistence (Postgre 15.x, JPA)
- Database migrations (Flyway)
- API documentation (springdoc-openapi)
- Application monitoring and health checks (Spring Boot Actuator)
- Database backup (pg_dump)

The purpose of Service Foo is to demonstrate **how a typical REST-based Spring Boot microservice can be structured and operated.**

Service Foo is not intended to provide a reference implementation for every possible combination of technologies or infrastructure capabilities. Instead, alternative technologies can be introduced in individual services when their specific requirements justify them.

### Maven Dependencies Used by the Reference Service

- Spring Boot Starter
- Spring Boot Starter Test
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Actuator
- PostgreSQL Driver
- Flyway
- springdoc-openapi-starter-webmvc-ui

These dependencies demonstrate a typical REST-based service with:

- HTTP/REST API endpoints
- Application/service-layer architecture
- Relational database persistence
- Database schema migrations
- API documentation
- Health and operational endpoints
- Automated testing

### Optional / Alternative Infrastructure and Integration Capabilities

The following capabilities are available as alternatives or additional capabilities for services with different requirements:

- **Alternative APIs / protocols** — WebSockets, gRPC, messaging, etc.
- **Caching** — e.g. Redis
- **Messaging** — e.g. Kafka, RabbitMQ
- **Security** — e.g. OAuth2, JWT
- **Alternative persistence technologies** — e.g. MongoDB, Cassandra

Service Foo does not attempt to demonstrate every possible combination of these capabilities. For example, there is no separate reference service for every combination of REST + Redis, REST + Kafka, gRPC + MongoDB, or WebSockets + OAuth2.

## Main Components - Microservice Architecture
```
API Client
    │
    ▼
┌──────────────────────────┐
│ Service Container        │
│                          │
│ Spring Boot              │
│ ├─ Controller            │
│ ├─ Service               │
│ ├─ Repository            │
│ └─ Flyway                │
│ OpenAPI / Swagger UI     │
│ Spring Boot Actuator     │
│ 
└────────────┬─────────────┘
             │ JDBC
             ▼
┌──────────────────────────┐
│ PostgreSQL Container     │
│                          │
│ PostgreSQL               │
│ pg_dump                  │
│                          │
│ backup.sh                │
│ cron                     │──────── backup ────────┐
└────────────┬─────────────┘                        │
             │                                      ▼
             ▼                            ┌────────────────────┐
┌──────────────────────────┐              │ Backup Storage     │
│ Persistent Volume        │              │ (outside server)   │
│                          │              │                    │
│ DB LIVE data             │              │ BACKUP data        │
└──────────────────────────┘              └────────────────────┘
```

### Component Responsibilities
- **Service Container** → runs the Spring Boot application.
- **PostgreSQL Container** → runs PostgreSQL and has pg_dump.
- **Persistent Volume** → contains live PostgreSQL data local to the server.
- **cron + backup.sh** → periodically trigger the backup.
- **pg_dump** → reads PostgreSQL and produces the backup.
- **Backup Storage** → stores the backup outside the server.
- **Flyway** → manages database schema migrations; it is not part of the backup process.
- **OpenAPI / Swagger UI** → provides API documentation for the service.
- **Spring Boot Actuator** → exposes operational information about the running application.


## Design Principles

The Service Foo application follows several key design principles to ensure maintainability, reliability, and consistency:

- **Representative reference architecture** — demonstrates a practical REST-based Spring Boot microservice rather than every possible technology combination.
- **Code quality** — Checkstyle and SpotBugs
- **Automated testing** — Spring Boot Starter Test
- **REST API** — Spring Boot Web
- **API documentation** — springdoc-openapi
- **Database persistence** — Spring Data JPA with PostgreSQL
- **Database migrations** — Flyway
- **Observability** — Spring Boot Actuator
- **Containerization** — Docker
- **Application/build versioning** — Maven
- **Database backup** — scheduled pg_dump backups to external storage
- **Loose coupling** — service-to-service communication through well-defined APIs

## Interactions with Other Services

Service Foo exposes RESTful APIs for clients and other services.

Example exposed endpoints: (http://localhost:8080) + endpoints
- GET `/v1/api/foo` ("Retrieve all Foo entities")
- POST `/v1/api/foo` ("Create a new Foo entity")
- `/actuator` ("Links to all available actuator endpoints")
- `/actuator/health` ("Overall health status of the application and its sub-components")
- `/actuator/health/liveness` ("Checks if the application is alive") 
- `/actuator/health/readiness` ("Checks if the application is ready to receive traffic") 
- `/actuator/info` ("Exposes application information")
- `/actuator/metrics` ("Exposes application metrics")
- `/v3/api-docs` ("OpenAPI specification in JSON format")
- `/swagger-ui/index.html` ("Swagger UI for interactive API documentation")

Service interactions should remain loosely coupled so that changes in one service have minimal impact on other services.
