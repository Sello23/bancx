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

## Testing Strategy

This project follows a strict TDD approach:
* **Unit Tests**: Testing business logic in isolation using Mockito (e.g., `PaymentServiceTest`).
* **Controller Tests**: Verifying API contracts and validation using `WebMvcTest` and `MockMvc` with `jsonPath` assertions.
* **Integration Tests**: Validating the full E2E lifecycle (Loan -> Payment -> History) using `TestRestTemplate` and an H2 database.

To run all tests:
```bash
./gradlew test

## ## Features

* **Loan Management**: Create and retrieve loans with automated status tracking (ACTIVE, SETTLED).
* **Payment Processing**: Real-time balance reduction with atomic transaction management.
* **Validation**: Strict input validation using Jakarta Bean Validation.
* **Error Handling**: Centralized exception management returning standardized `ProblemDetail` responses.
* **Persistence**: Fast, in-memory H2 database for rapid development and testing.

---

### ### Installation
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/bancx/sello-loan-system.git](https://github.com/bancx/sello-loan-system.git)
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd sello-loan-system
    ```
3.  **Build the project:**
    ```bash
    ./gradlew build
    ```

### ### Running the Application
```bash
./gradlew bootRun

Below are a list of curl commands that prove the functionality of the endpoints

---

## Interactive API Sandbox

Use these commands to manually verify the logic defined in our unit tests. 

### 1. [Test] Create Loan
**Unit Test Match:** `shouldCreateLoan()`
```bash
curl -X POST http://localhost:8080/loans \
-H "Content-Type: application/json" \
-d '{"loanAmount": 1000.00, "term": 12}'

From the LoanId that you get from the above:

curl -i -X POST http://localhost:8080/payments \
-H "Content-Type: application/json" \
-d '{
    "loadId": "{loanId}",
    "paymentAmount": 200.00
}'

Check that the loan balance is now 800.00
curl -X GET http://localhost:8080/loans/{loanId}

Make a final payment for the remaining balance (800.00). This should trigger the code to flip the status to SETTLED
curl -i -X POST http://localhost:8080/payments \
-H "Content-Type: application/json" \
-d '{
    "loanId": "{loanId}",
    "paymentAmount": 800.00
}'

Run this to see if the status is now SETTLED and balance is 0.00.
curl -X GET http://localhost:8080/loans/{loanId}
