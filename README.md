# Transaction Decision Engine

## About This Project

I built the **Transaction Decision Engine** as a personal engineering project to showcase my backend development skills and demonstrate how I approach the design and implementation of transaction-based systems.

My professional experience has given me exposure to enterprise Java applications, integrations, transaction processing, testing and production support. This project gives me a space to apply those skills independently while also working with technologies and architectural patterns beyond what I use in my day-to-day environment.

The application simulates a financial transaction decisioning service. It receives a transaction, validates the request, applies decision rules, persists the result and returns **APPROVED**, **REVIEW** or **DECLINED**.

This project is independent and does not contain proprietary code or implementation details from systems I have worked on.

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

The first version deliberately uses an embedded H2 database so the application can be cloned and run without external infrastructure. More production-oriented infrastructure can be introduced incrementally as the project evolves.

## Decision Rules

| Amount | Decision |
|---|---|
| Up to R10,000 | APPROVED |
| Above R10,000 up to R50,000 | REVIEW |
| Above R50,000 | DECLINED |

## Run the Project

Requirements:

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

JDBC URL: `jdbc:h2:mem:transactiondb`  
User: `sa`  
Password: leave blank

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

Expected decision:

```json
{
  "transactionId": "TX-10001",
  "decision": "APPROVED",
  "reason": "ALL_CHECKS_PASSED"
}
```

## Development Approach

I am building this project incrementally. The goal is not to collect technologies, but to introduce each one when it solves a real engineering problem and to understand the architectural decisions and trade-offs behind it.
