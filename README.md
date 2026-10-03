
# Transaction Decision Engine

A Spring Boot REST API that simulates transaction decisioning by validating incoming transactions, applying business rules, persisting results, and returning an APPROVED, REVIEW, or DECLINED decision.

## About This Project

I built the **Transaction Decision Engine** as a personal engineering project to showcase my backend development skills and demonstrate how I approach the design and implementation of transaction-based systems.

My professional experience has given me exposure to enterprise Java applications, integrations, transaction processing, testing, and production support. This project gives me a space to apply those skills independently while working through the full development lifecycle from implementation to CI/CD and cloud deployment.

This project is independent and does not contain proprietary code or implementation details from systems I have worked on.

## Live Demo

The application is deployed as a Docker container in the cloud.

**Swagger UI:**  

`https://transaction-decision-engine-latest-1.onrender.com/swagger-ui/index.html#/transaction-controller/decide`

**Health Check:**  
`https://transaction-decision-engine-latest.onrender.com/actuator/health`

> The application is hosted on a free cloud instance and may take a short time to start after a period of inactivity.

## Architecture

```text
Client
  |
  v
REST Controller
  |
  v
Transaction Decision Service
  |
  +----> Decision Engine
  |
  v
Spring Data JPA
  |
  v
H2 Database
```

Deployment flow:

```text
GitHub
   |
   v
GitHub Actions
   |
   v
Docker Build
   |
   v
GitHub Container Registry (GHCR)
   |
   v
Render Cloud
```

## Skills Demonstrated

- Java 17
- Spring Boot
- REST API design
- Bean Validation
- Service and persistence layers
- Business-rule implementation
- Idempotent request handling
- Spring Data JPA
- H2 relational database
- Exception handling
- JUnit / Spring Boot testing
- OpenAPI / Swagger
- Spring Boot Actuator
- Docker
- GitHub Actions
- CI/CD
- GitHub Container Registry
- Cloud deployment

## Decision Rules

| Amount | Decision |
| --- | --- |
| Up to R10,000 | APPROVED |
| Above R10,000 up to R50,000 | REVIEW |
| Above R50,000 | DECLINED |

## Run Locally

### Requirements

- Java 17
- Maven

Run tests:

```bash
mvn clean test
```

Start the application:

```bash
mvn spring-boot:run
```

Swagger UI:

`http://localhost:8080/swagger-ui.html`

Health check:

`http://localhost:8080/actuator/health`

H2 console:

`http://localhost:8080/h2-console`

Database configuration:

```text
JDBC URL: jdbc:h2:mem:transactiondb
User: sa
Password: leave blank
```

## Example Request

```bash
curl --location 'http://localhost:8080/api/v1/transactions/decisions' \
--header 'Content-Type: application/json' \
--header 'Idempotency-Key: demo-001' \
--data '{
  "transactionId": "TX-10001",
  "customerId": "CUS-1001",
  "accountId": "ACC-5001",
  "amount": 1500.00,
  "currency": "ZAR",
  "transactionType": "ONLINE_PURCHASE",
  "merchantId": "MER-8872",
  "merchantCategory": "ELECTRONICS",
  "channel": "WEB",
  "countryCode": "ZA",
  "transactionTimestamp": "2026-09-29T10:00:00Z"
}'
```

Example response:

```json
{
  "transactionId": "TX-10001",
  "decision": "APPROVED",
  "reason": "ALL_CHECKS_PASSED"
}
```

## Idempotency

The decision endpoint requires an `Idempotency-Key` header.

This helps prevent the same request from being processed multiple times and demonstrates an important consideration when designing transaction-processing APIs.

## Containerisation and CI/CD

The application is packaged as a Docker image using a multi-stage Docker build.

When changes are pushed to the `main` branch, GitHub Actions:

1. Checks out the source code.
2. Builds the Docker image.
3. Authenticates with GitHub Container Registry.
4. Publishes the latest image to GHCR.

The published container image is then used to run the application in Render.

## Development Approach

I am building this project incrementally. The goal is not to add technologies simply for the sake of complexity, but to introduce them when they solve a clear engineering problem.

The first version uses an embedded H2 database so the application can run without external database infrastructure. The project can be extended with more production-oriented infrastructure as it evolves.
Development Note: AI-assisted tools were used for guidance and troubleshooting during development.