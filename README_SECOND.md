# BancX Loan Management System

A professional, TDD-driven Spring Boot application for managing loan lifecycles and payment processing. 
This project demonstrates Clean Architecture principles, Domain-Driven Design (DDD), and robust error handling using RFC 7807.

---

## Getting Started

The server will start at `http://localhost:8080`.
* **H2 Console:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:loandb`

---

## API Documentation

### Loan Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/loans` | Create a new loan. |
| `GET` | `/loans/{loanId}` | Retrieve loan details and current balance. |

### Payment Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/payments` | Process a payment against a loan. |
| `GET` | `/payments/loan/{loanId}` | Retrieve transaction history for a specific loan. |

---

## Design Patterns & Decisions

* **DTO Pattern**: Separate Request/Response DTOs are used to ensure the internal Domain Entities (DAO) never leak 
* to the API consumer.
* **Static Factory Methods**: Mapping logic (e.g., `fromEntity`) is encapsulated within DTOs for cleaner controllers.
* **RFC 7807**: All errors (404 Not Found, 409 Conflict, etc.) are returned as "Problem Details" to provide consistent, 
* machine-readable error context.
* **Transactional Integrity**: The `@Transactional` boundary in `PaymentService` ensures that loan balances are only 
* updated if the payment record is successfully persisted.

---

## Testing Strategy

This project follows a strict TDD approach:
* **Unit Tests**: Testing business logic in isolation using Mockito (e.g., `PaymentServiceTest`).
* **Controller Tests**: Verifying API contracts and validation using `WebMvcTest` and `MockMvc` with `jsonPath` assertions.
* **Integration Tests**: Validating the full E2E lifecycle (Loan -> Payment -> History) using `TestRestTemplate` and an H2 database.

To run all tests:
```bash
./gradlew test