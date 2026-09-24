# Service Manual Steps

This document provides the manual steps to create self-contained deployments of the service `service-foo`. The steps are listed sequentially for clarity.

## ToC
1. [Define the service name and its purpose](#1-define-the-service-name-and-its-purpose)
2. [Spring Boot Initializr (Generate service-foo project structure)](#2-spring-boot-initializr-generate-service-foo-project-structure)
3. [Unzip to `maven-shared/services`](#3-unzip-to-maven-sharedservices)
4. [Validate the project setup](#4-validate-the-project-setup)
5. [Update the pom.xml](#5-update-the-pomxml)
6. [Validate the pom.xml updates](#6-validate-the-pomxml-updates)
7. [Implement the minimal REST + persistence layer](#7-implement-the-minimal-rest--persistence-layer)<br>
7.1 [Create PostgreSQL infrastructure](#71-create-postgresql-infrastructure)<br>
7.2 [Create Flyway migration](#72-create-flyway-migration)<br>
7.3 [Create entity](#73-create-entity)<br>
7.4 [Create repository](#74-create-repository)<br>
7.5 [Create service](#75-create-service)<br>
7.6 [Create controller](#76-create-controller)<br>
7.7 [Test Endpoints](#77-test-endpoints)<br>
7.8 [Add OpenAPI Documentation](#78-add-openapi-documentation)<br>
7.9 [Add/Adjust Tests](#79-addadjust-tests)<br>
8. [Add Actuator](#8-add-actuator)<br>
9. [Containerize the application](#9-containerize-the-application)
10. [Logging to Infrastructure](#10-logging-to-infrastructure)<br>
10.1 [Configure Logging for service](#101-configure-logging-for-service)<br>
10.2 [Configure fluentd](#102-configure-fluentd)<br>
10.3 [Configure Fluent Bit](#103-configure-fluent-bit)<br>
10.4 [Configure Elasticsearch](#104-configure-elasticsearch)<br>
10.5 [Configure Structured JSON Logging](#105-configure-structured-json-logging)<br>
11. [Log Lifecycle Management](#11-log-lifecycle-management)<br>
11.1 [Create ILM Policy](#111-create-ilm-policy)<br>
11.2 [Create Index Template with ILM Policy](#112-create-index-template-with-ilm-policy)<br>
11.3 [Create initial index with write alias](#113-create-initial-index-with-write-alias)<br>
12. [Disk Watermark Settings](#12-disk-watermark-settings)


---

## 1. Define the service name and its purpose

```text
- Name:service-foo
- Coordinates: com.devon90.service.service-foo:0.0.0
- repository: https://github.com/devon90/maven-shared 
- repo sub-dir: services/service-foo
- Base Features:
    - Java 17
    - Spring Boot
    - Maven
    - testing
    - Checkstyle
    - SpotBugs
    - CI
    - logging
    - configuration`
    - health checks
    - containerization
    - versioning
- Optional Features:
    - API REST
    - OpenAPI/Swagger
    - DB PostgreSQL
    - Migration (Flyway)
- Needed Maven Dependencies:
    - Spring Boot Starter
    - Spring Boot Starter Test
    - Spring Boot Starter Data JPA
    - PostgreSQL Driver
    - Spring Boot Starter Web
    - Spring Boot Starter Actuator
    - Flyway
    - Springdoc OpenAPI Starter
    
- Needed Maven Plugins:
    - Checkstyle Plugin
    - SpotBugs Plugin
    - Spring Boot Maven Plugin
- Purpose: Reference demonstrator with minimum source code to demonstrate the baseline service structure and conventions.
```
---

## 2. Spring Boot Initializr (Generate service-foo project structure)

1. Language = `Java`
2. Type = `Maven Project`
3. Spring Boot Version = `4.1.1`
4. Group = `com.devon90.services`
5. Artifact = `service-<service-name>`
6. Package name = `com.devon90.services.service.<service-name>`
7. Packaging = `jar`
8. Configuration = `application.properties`
9. Java Version = `17`
10. Add deps: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, `Spring Boot Actuator`, `Flyway`, `Springdoc OpenAPI Starter`
11. Generate the project.

---

## 3. Unzip to maven-shared/services

1. Go to ~/Downloads
2. Unzip the generated project to `maven-shared/services`
3. The final structure should be `maven-shared/services/service-<service-name>`

---

## 4. Validate the project setup

1. navigate to `maven-shared/services/service-<service-name>`
2. run `./mvnw clean compile`

---

## 5. Update the pom.xml

1. `<version>0.0.0</version>`
2. Install plugin `checkstyle`.
3. Create `config/checkstyle/checkstyle.xml` with the desired Checkstyle rules.
4. Install plugin `spotbugs`.

---

## 6. Validate the pom.xml updates

1. navigate to `maven-shared/services/service-<service-name>`
2. run `./mvnw clean compile` to ensure that the project compiles successfully with the updated `pom.xml` and plugins.

---

## 7. Implement the minimal REST + persistence layer

### 7.1 Create PostgreSQL infrastructure
Create PostgreSQL infrastructure in `docker/docker-compose.yml`.

1. Run `docker compose -f docker/docker-compose.yml up -d`
2. Check local volume existance
   - run `docker volume ls` to check volume name
   - run `docker volume inspect docker_postgres_data` for mountpoint path
    - run `ls <mountpoint path>`

---

### 7.2 Create Flyway migration

1. create folder/file `src/main/resources/db/migration/V1__create_foo_table.sql` (important two underscores)
2. Update `application.properties` with the database connection details.
   - spring.datasource.url=jdbc:postgresql://localhost:5432/service_foo
   - spring.datasource.username=service_foo
   - spring.datasource.password=service_foo
3. run `./mvnw clean test builds` the project and executes all available tests.
4. Verify the migration with `pgAdmin4` and `Portainer`
   - Open `Portainer`, find pgAdmin network and remember name
   - In `Portainer`, find postgres container, go to connect networks, select the pgAdmin network remembered earlier and Join it.
   - Open `pgAdmin4` tab `Object/register/server` and register the postgres server.
      - Name: service-foo-postgres (container name)
      - Hostname/address: service-foo-postgres (container name)
      - Port: 5432
      - Username: service-foo
      - Password: service-foo
   - Verify that the `V1__create_foo_table.sql` migration has been applied successfully by checking table existance in `pgAdmin4`.
      - confirm foo table exists
      - confirm flyway_schema_history table exists

---

### 7.3 Create entity

1. Create `Foo` entity class in `src/main/java/com/devon90/services/service_foo/entity/Foo.java`.
2. run `./mvnw clean test` builds the project and executes all available tests

---

### 7.4 Create repository

1. Create `src/main/java/com/devon90/services/service_foo/repository/FooRepository.java`
2. run `./mvnw clean test` builds the project and executes all available tests.

---

### 7.5 Create service

1. Create `src/main/java/com/devon90/services/service_foo/service/FooService.java`
2. run `./mvnw clean test` builds the project and executes all available tests.

---

### 7.6 Create controller

1. Create `src/main/java/com/devon90/services/service_foo/controller/FooController.java`.
2. run `./mvnw clean test` builds the project and executes all available tests.

---

### 7.7 Test Endpoints

1. Start postgres container if not already running.
2. Start service-foo app `./mvnw spring-boot:run`.
3. run `curl http://localhost:8080/v1/api/foo` expect [] as the response since the table is initially empty.
4. run 
   ```
   curl -X POST http://localhost:8080/v1/api/foo \
     -H "Content-Type: application/json" \
     -d '{"name":"Example"}'
    ``` 
    to create a new `foo` entry.

---

### 7.8 Add OpenAPI documentation

1. Open autogenerated `http://localhost:8080/v3/api-docs` 
2. Open autogenerated Swagger UI at `http://localhost:8080/swagger-ui.html` to view the OpenAPI documentation.

---

### 7.9 Add/adjust tests

1. Create Controller test.
2. Create test `src/test/java/com/devon90/services/service_foo/controller/FooControllerTest.java` for `GET` and `POST` endpoints.
3. Run `./mvnw clean test` builds the project and executes all available tests.
4. Create persistence integration test
5. Create test `src/test/java/com/devon90/services/service_foo/repository/FooRepositoryTest.java`
6. run `./mvnw clean test` builds the project and executes all available tests.
7. Create test `src/test/java/com/devon90/services/service_foo/service/FooServiceTest.java` for service layer.
8. Run `./mvnw clean test` builds the project and executes all available tests.

---

## 8. Add Actuator

1. Open autogenerated actuator endpoint at `http://localhost:8080/actuator`.
2. Open autogenerated health check endpoint at `http://localhost:8080/actuator/health`.
3. Open autogenerated health check details endpoint at `http://localhost:8080/actuator/health/liveness`.
4. Open autogenerated health check details endpoint at `http://localhost:8080/actuator/health/readiness`.
5. Expose actuator info and metrics endpoints at:
   - `http://localhost:8080/actuator/info` and 
   - `http://localhost:8080/actuator/metrics`.
6. Update `application.properties` with
   - `management.endpoints.web.exposure.include=health,info,metrics`
7. Restart the application to apply the changes.
8. Verify with curl
   - `curl http://localhost:8080/actuator/info`
   - `curl http://localhost:8080/actuator/metrics`
9. Adjust behaviour of readiness and liveness probes 
   - if BD is down & app is UP = readiness → DOWN, liveness → UP.
   - if BD is up & app is down = readiness → DOWN, liveness → DOWN.
10. Add to `application.properties`
    - `management.endpoint.health.group.readiness.include=readinessState,db`
11. Restart the application to apply the changes.
12. Stop postgres container if running `docker stop <container_name>`.
13. Verify endpoint statuses with curl
    - `curl http://localhost:8080/actuator/health` = `{"status":"DOWN"}`
    - `curl http://localhost:8080/actuator/health/liveness` = `{"status":"UP"}`
    - `curl http://localhost:8080/actuator/health/readiness` = `{"status":"DOWN"}`
14. Start postgres container if needed `docker start <container_name>`.
15. Enable `health details` endpoint in `application.properties`
    - `management.endpoint.health.show-details=always`
16. Restart the application to apply the changes.
    - `curl http://localhost:8080/actuator/health` includes details
    - `curl http://localhost:8080/actuator/health/readiness` includes details

---

## 9. Containerize the application

Containerize application `services/service-foo`

1. Build application JAR for `service-foo`
    ```bash
    cd services/service_foo
    ./mvnw clean package
    ```
2. Create  Dockerfile<br>
   Create a `Dockerfile` in the `services/service_foo/docker/Dockerfile`
   ```dockerfile
   FROM eclipse-temurin:17-jre

   WORKDIR /app

   COPY target/service-foo-0.0.0.jar app.jar

   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```
3. Build the Docker image
   ```bash
   cd services/service-foo
   docker build -f docker/Dockerfile -t service-foo:0.0.0 .
   ```
4. Networking Update
   Update `docker-compose.yml` to include the service-foo container.
   ```yml
   // Add the service-foo container to configuration 
   service-foo:
     image: service-foo:0.0.0
     container_name: service-foo
     ports:
       - 8080:8080
     environment:
       - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/service_foo
       - SPRING_DATASOURCE_USERNAME=service_foo
       - SPRING_DATASOURCE_PASSWORD=service_foo
     depends_on:
       - postgres
   ```
5. Run the Docker container for service-foo
   ```bash
   cd services/service-foo
   docker compose -f docker/docker-compose.yml down
   docker compose -f docker/docker-compose.yml up -d
   ```

---

## 10. Logging to Infrastructure
Applied architecture for logging to infrastructure:
```text
Application 
    ↓ 
Docker fluentd logging driver 
    ↓ 
Fluent Bit 
    ↓ 
Elasticsearch
    ↓
persistent storage
```

---

### 10.1 Configure Logging for service

1. Add Logging levels to `application.properties`
   ```properties
   logging.level.com.devon90.services.service_foo=TRACE
   logging.level.root=INFO
   ```
2. Add logging to service class (`ServiceFoo`)
   ```java
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;

   public void someMethod() {
        logger.trace("Trace message");
        logger.debug("Debug message");
        logger.info("Info message");
        logger.warn("Warn message");
        logger.error("Error message");
   } 
   ```
   Now service logs into Docker. Verify with `docker logs <container_name>`.

---

### 10.2 Configure fluentd 

1. Configure Docker to send service-foo logs to Fluent Bit by adding the following to docker/docker-compose.yml under service-foo:
   ```yml
   logging:
     driver: fluentd
     options:
        fluentd-address: "127.0.0.1:24224"
        fluentd-async: "true"
        tag: "{{.Name}}"
   ```
---

### 10.3 Configure Fluent Bit

1. Create the Fluent Bit configuration
   
   Create a file `docker/fluent-bit/fluent-bit.conf`:
   ```ini
   [SERVICE]
        Flush        1
        Log_Level    info

   [INPUT]
        Name         forward
        Listen       0.0.0.0
        Port         24224

   [OUTPUT]
        Name         stdout
        Match        *
   ```
5. Add Fluent Bit to Docker Compose
   ```yml
   fluent-bit:
     image: cr.fluentbit.io/fluent/fluent-bit:latest
     container_name: fluent-bit

     ports:
        - "24224:24224"

     volumes:
        - ./fluent-bit/fluent-bit.conf:/fluent-bit/etc/fluent-bit.conf:ro
   ```
6. Start the stack
   ```bash
   docker compose -f docker/docker-compose.yml down
   docker compose -f docker/docker-compose.yml up -d
   ```
   Now logs are forwarded to Docker from both service-foo and Fluent Bit.

7. Verify that the Fluent Bit container is running and collecting logs
   ```bash
   docker ps | grep fluent-bit
   ```
   ```bash
   docker logs fluent-bit
   ```
8. Generate a log for `service-foo`
   ```bash
   curl http://localhost:8080/v1/api/foo
   ```
   Then check the Fluent Bit logs to see if the log from `service-foo` has been collected.
   ```bash
   docker logs fluent-bit
   ```

---

### 10.4 Configure Elasticsearch

1. Add Elasticsearch service to `docker/docker-compose.yml`
    ```yml
    elasticsearch:
        image: docker.elastic.co/elasticsearch/elasticsearch:8.19.0
        container_name: elasticsearch
        environment:
            - discovery.type=single-node
            - xpack.security.enabled=false
            - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
        ports:
            - "127.0.0.1:9200:9200"
        healthcheck:
            test: ["CMD-SHELL", "curl -fs http://localhost:9200 > /dev/null || exit 1"]
            interval: 5s
            timeout: 5s
            retries: 10
            start_period: 10s
    ```
2. Add Healthcheck of Elasticsearch to fluent-bit service
    ```yml
    fluent-bit:
        depends_on:
            elasticsearch:
                condition: service_healthy
    ```
3. Start the stack with the new Elasticsearch service
   ```bash
   docker compose -f docker/docker-compose.yml down
   docker compose -f docker/docker-compose.yml up -d
   ```
4. Verify that the Elasticsearch container is running
   ```bash
   docker ps | grep elasticsearch
   ```
5. Access Elasticsearch to verify it is working.
   ```bash
   curl http://localhost:9200
   ```
6. Configure Fluent Bit to send its records to Elasticsearch.
   
   Change in docker/fluent-bit/fluent-bit.conf:<br>
   from
   ```ini
   [OUTPUT]
      Name         stdout
      Match        *
   ```
   to
   ```ini
   [OUTPUT]
      Name         es
      Match        *
      Host         elasticsearch
      Port         9200
      Index        service-logs
      Suppress_Type_Name On
   ```
7. Restart the stack to apply the Fluent Bit configuration changes.
   ```bash
   docker compose -f docker/docker-compose.yml down
   docker compose -f docker/docker-compose.yml up -d
   ```
8. Verify that Fluent Bit is sending logs to Elasticsearch.
   ```bash
   curl http://localhost:9200/service-logs/_search?pretty
   ```

---

### 10.5 Configure Structured JSON Logging

1. Configure `application.properties` for structured JSON logging.
   ```properties
   # Structured JSON logging 
   logging.structured.format.console=ecs
   ```
2. Rebuild package to apply the structured JSON logging configuration. Update new version in `pom.xml`
   ```xml
   <version>NEW_VERSION</version>
   ```
3. Update docker/Dockerfile with the new version.
   ```dockerfile
   COPY target/service-foo-NEW_VERSION.jar app.jar
   ```
4. Update docker/docker-compose.yml with the new image version if necessary.
   ```yml
   services:
    service-foo:
        image: service-foo:NEW_VERSION
   ```
5. Build the package
   ```bash
   cd services/service-foo
   ./mvnw clean package
   ```
6. Build the Docker image with the new version.
   ```bash
   cd services/service-foo
   docker build -f docker/Dockerfile -t service-foo:NEW_VERSION .
   ```
7. Build the stack with the new Docker image.
   ```bash
   docker compose -f docker/docker-compose.yml down
   docker compose -f docker/docker-compose.yml up -d
   ```
8. Create fluent-bit parser
   Add a file `docker/fluent-bit/parsers.conf` with the following content.
   ```ini
   [PARSER]
      Name        spring_json
      Format      json
      Time_Key    @timestamp
      Time_Format %Y-%m-%dT%H:%M:%S.%L%z
      Time_Keep   On
   ```
9. Register fluent-bit parser volume to `fluent-bit` container
   
   Add the following volume configuration to the `fluent-bit` service in `docker/docker-compose.yml`.
   ```yml
   services:
    fluent-bit:
        volumes:
            - ./fluent-bit/parsers.conf:/fluent-bit/parsers.conf:ro
   ```
10. Add fluent-bit parser configuration to `docker/fluent-bit/fluent-bit.conf`

    Add the following parser configuration.
    ```ini
    [SERVICE]
       ...
       Parsers_File /fluent-bit/parsers.conf
    ```
11. Add fluelt-bit filter configration to `docker/fluent-bit/fluent-bit.conf`
    ```ini
    [FILTER]
       Name         parser
       Match        *
       Key_Name     log
       Parser       spring_json
       Reserve_Data On
    ```
12. Reload Docker Compose stack
    
    After adding persistent storage for Elasticsearch, reload the Docker Compose stack to apply the changes.
    ```bash
    docker compose -f docker/docker-compose.yml down
    docker compose -f docker/docker-compose.yml up -d
    ```
13. Verify Elasticsearch persistent storage

    After reloading the Docker Compose stack, verify that Elasticsearch is using the persistent storage by checking the Docker volumes.

    ```bash
    docker volume ls
    ```
    ```bash
    curl http://localhost:9200/_count?pretty
    ```
    If `count` grows after restarting the Docker Compose stack, it indicates that Elasticsearch is successfully using the persistent storage.

---

## 11. Log Lifecycle Management

Rules definition:

| Rule | Description |
|------|-------------|
| rollover - time | max_age = 24h |
| rollover - size | max_size = 1gb |
| retention - time | min_age = 30d |
| action - rollover | Index indices are rolled over when the specified conditions are met. |
| action - delete | Old indices are deleted when the specified retention conditions are met. |

### 11.1 Create ILM Policy

1. Create `docker/elasticsearch/ilm-policy.json` with configuration:
   
   ```json
   {
    "policy": {
        "phases": {
        "hot": {
            "actions": {
            "rollover": {
                "max_age": "24h",
                "max_primary_shard_size": "1gb"
            }
            }
        },
        "delete": {
            "min_age": "30d",
            "actions": {
            "delete": {}
            }
        }
        }
    }
    }
   ```
2. Apply the ILM policy to the Elasticsearch index template.
 
   ```bash
   cd services/service-foo
   curl -X PUT \
    "http://localhost:9200/_ilm/policy/service-logs-policy" \
    -H "Content-Type: application/json" \
    --data-binary @docker/elasticsearch/ilm-policy.json
   ```
3. Validate rollover policy
   
   ```bash
   curl "http://localhost:9200/_ilm/policy/service-logs-policy?pretty"
   ```

---

### 11.2 Create Index Template with ILM Policy
 
1. `Create docker/elasticsearch/index-template.json` with configuration:
   ```json
   {
    "index_patterns": [
        "service-logs-*"
    ],
    "template": {
        "settings": {
        "index.lifecycle.name": "service-logs-policy",
        "index.lifecycle.rollover_alias": "service-logs-write",
        "number_of_replicas": 1
        }
    }
    }
   ```
2. Apply the index template to Elasticsearch.
   ```bash
   cd services/service-foo
   curl -X PUT \
     "http://localhost:9200/_index_template/service-logs-template" \
     -H "Content-Type: application/json" \
     --data-binary @docker/elasticsearch/index-template.json
   ```
3. Validate index template
   ```bash
   curl "http://localhost:9200/_index_template/service-logs-template?pretty"
   ```

---

### 11.3 Create initial index with write alias
    
1. Create the initial index with the write alias:

    ```json
    {
        "aliases": {
        "service-logs-write": {
            "is_write_index": true
           }
        }
    }
    ```
2. Apply initial index
   ```bash
   cd services/service-foo
   curl -X PUT \
    "http://localhost:9200/service-logs-000001" \
    -H "Content-Type: application/json" \
    --data-binary @docker/elasticsearch/initial-index.json
   ```
3. Validate initial index
   ```bash
   curl "http://localhost:9200/service-logs-000001?pretty"
   ```
4. Validate ILM management of initial index
   ```bash
   curl "http://localhost:9200/service-logs-000001/_ilm/explain?pretty"
   ```

   Expected Key Outputs:
   - **"managed": true** // Elasticsearch ILM is actively managing the index
   - **"policy": "service-logs-policy"** // The correct policy is attached
   - **"phase": "hot"** // The index is currently in the hot phase.
   - **"action": "rollover"** // ILM is currently evaluating the rollover action.
   - **"step": "check-rollover-ready"** // Elasticsearch is currently checking whether the index has reached one of the rollover conditions.
5. Configure Fluent Bit to use the rollover write alias

   Update `docker/fluent-bit/fluent-bit.conf` to use the rollover write alias from:
   ```ini
   [OUTPUT]
      ...
      Index        service-logs
      ...
   ```
   to
   ```ini
   [OUTPUT]
      ...
      Index        service-logs-write
      ...
   ```
6. Reload stack
   ```bash
   docker compose -f docker-compose.yml down
   docker compose -f docker-compose.yml up -d
   ```
7. Validate document count in initial index
   ```bash
   curl "http://localhost:9200/service-logs-000001/_count?pretty"
   ```
8. Validate the most recent document in initial index
   ```bash
   curl "http://localhost:9200/service-logs-000001/_search?size=1&sort=@timestamp:desc&pretty"
   ```
 
---

## 12. Disk Watermark Settings

- ⚠️ Close relation to disk usage and Elasticsearch index management.
- ⚠️ Following steps are `informative only` as default setting has not been modified.

1. Check current disk watermark settings in Elasticsearch:
   ```bash
    curl "http://localhost:9200/_cluster/settings?include_defaults=trueflat_settings=true&pretty" \
    | grep -E "watermark"
   ```
   or
   ```bash
   // in browser, navigate to:
   http://localhost:9200/_cluster/settings?include_defaults=true&flat_settings=true&pretty
   ```

   Keep default watermarks as configured by Elasticsearch. If for any reason you need to change them:

2. Overriding default disk watermarks

   Create a JSON file `docker/elasticsearch/watermark-default-override.json` with the new watermark settings: 

   ```json
   {
    "persistent": {
        "cluster.routing.allocation.disk.watermark.low": "80%",
        "cluster.routing.allocation.disk.watermark.low.max_headroom": "180GB"
      }
    }   
   ```
   Some of features can be configured solely, but some must be configured in conjunction with other settings. Check documentation for details.

   Apply the new watermark settings by running:
   ```bash
   curl -X PUT "http://localhost:9200/_cluster/settings" \
     -H 'Content-Type: application/json' \
     --data-binary @docker/elasticsearch/watermark-default-override.json
   ```

   These settings will override the default disk watermarks for the Elasticsearch cluster.

   To return back to the default watermarks, you need to reset values to null:

   ```json
   {
    "persistent": {
        "cluster.routing.allocation.disk.watermark.low": null,
        "cluster.routing.allocation.disk.watermark.low.max_headroom": null
      }
    }
   ```
   This will erase the custom disk watermark settings and revert to the default values configured by Elasticsearch.

---