
# Robot Telemetry Service

A Spring Boot backend service for real-time telemetry ingestion and monitoring of a robotics fleet using Kafka, PostgreSQL, and REST APIs.

# Project Purpose

This project is designed to receive telemetry data from various types of robots (ground and aerial),
store it in a PostgreSQL database, and provide real-time monitoring. The service also supports
rule-based alerting triggered by threshold values (e.g., low battery level, high temperature, etc.).


## Kafka Integration and Scalability

The service leverages a Kafka client to decouple MQTT-based robot messages from downstream processing. A dedicated MQTT gateway receives telemetry from robots and publishes them to corresponding Kafka topics based on robot type.

This architecture ensures scalability. As the number of robots grows, no data is lost—new service instances can simply be added to the Kafka consumer group to handle increased load.

To support new robot types, just define and route to a new Kafka topic.

## Data Optimization Strategy

- **Indexing**: Apply indexes on frequently queried fields to ensure performance.
- **Sharding**: Consider sharding large tables as telemetry volume scales.
- **Data Retention**: Old data can be deleted or archived into a `history` table for long-term storage and analytics.
---

# Project Structure

```
src/
├── main/
│   ├── java/com/robottelemetry/
│   │   ├── RobotTelemetryApplication.java
│   │   ├── adapter/               # Queries to DB and transformation logic
│   │   ├── configuration/         # App-level configurations (Kafka, WebSocket)
│   │   ├── controller/            # REST API endpoints
│   │   ├── dto/                   # Data transfer objects
│   │   ├── enums/                 # Enum definitions
│   │   ├── excaption/             # Custom exceptions
│   │   ├── listener/              # Kafka topic consumers
│   │   ├── model/                 # Core domain models
│   │   ├── repository/            # Spring Data JPA repositories
│   │   │   ├── entity/            # JPA entities
│   │   └── service/               # Business logic and alerting
│   └── resources/
│       ├── application.yml        # Main config file
│       ├── logback-spring.xml     # Logging configuration
│       ├── static/                # Web static files (if any)
│       └── templates/             # HTML templates (if any)
├── test/
│   └── java/com/robot_telemetry/
│       ├── RobotTelemetryApplicationTests.java
│       └── RobotTelemetryServiceTest.java
```

---

## ERD - Entity Relationship Diagram

### Key Entities

- **BaseRobotTelemetryEntity** (abstract): Shared fields for all robot types
- **GroundRobotTelemetryEntity**: Adds ground robot specifics
- **AerialRobotTelemetryEntity**: Adds aerial (drone) specifics 
- **AlertEntity**: Stores rule-based alerts
- **RobotStatusEntity**: Stores latest robot status by ID

---

# Tech Stack

- Java 17
- Spring Boot
- Kafka (Bitnami)
- PostgreSQL
- Docker & Docker Compose
- Kubernetes-ready (YAML files provided)

---

# Running the Project

```bash
# Build the project
./mvnw clean package

# Run with Docker Compose
docker-compose up --build
```

---

# Test Endpoints

- `GET /robots` — List all active robots
- `GET /robots/{id}/telemetry` — Recent telemetry
- `POST /alerts/rules` — Create new alert rules

---
