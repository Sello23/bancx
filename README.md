# BancX Loan Management System

A professional, TDD-driven Spring Boot application for managing loan lifecycles and payment processing. 
This project demonstrates Clean Architecture principles, Domain-Driven Design (DDD), and robust error handling using RFC 7807.

---

## Features

* **Loan Management**: Create and retrieve loans with automated status tracking (ACTIVE, SETTLED).
* **Payment Processing**: Real-time balance reduction with atomic transaction management.
* **Validation**: Strict input validation using Jakarta Bean Validation.
* **Error Handling**: Centralized exception management returning standardized `ProblemDetail` responses.
* **Persistence**: Fast, in-memory H2 database for rapid development and testing.

---

## Getting Started

The server will start at `http://localhost:8080`.
* **H2 Console:** `http://localhost:8080/h2-console`
* **JDBC URL:** `jdbc:h2:mem:loandb`

### Installation
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

### Running the Application
```bash
./gradlew bootRun


📖 API DocumentationLoan

EndpointsMethodEndpointDescriptionPOST/loansCreate a new loan.
GET/loans/{loanId}Retrieve loan details and current balance.
Payment EndpointsMethodEndpointDescriptionPOST/paymentsProcess a payment against a loan.
GET/payments/loan/{loanId}Retrieve transaction history for a specific loan.

Testing Strategy

This project follows a strict TDD approach:

Unit Tests: Testing business logic in isolation using Mockito (e.g., PaymentServiceTest).Controller Tests: Verifying API contracts and validation using WebMvcTest and MockMvc with jsonPath assertions.

Integration Tests: Validating the full E2E lifecycle (Loan -> Payment -> History) using TestRestTemplate and an H2 database.

To run all tests:Bash./gradlew test

Interactive API Sandbox (CURL)Use these commands to manually verify the logic defined in our unit tests.

1. [Test] Create LoanUnit Test Match: shouldCreateLoan()Bashcurl -X POST http://localhost:8080/loans \
-H "Content-Type: application/json" \
-d '{"loanAmount": 1000.00, "term": 12}'
Take note of the loanId returned in the response for the following steps.2. [Test] Process Partial PaymentScenario: Pay R200.00 toward the loan.Bashcurl -i -X POST http://localhost:8080/payments \
-H "Content-Type: application/json" \
-d '{
    "loanId": "{loanId}",
    "paymentAmount": 200.00
}'
3. [Test] Verify Partial BalanceScenario: Check that the loan balance is now 800.00 and status is ACTIVE.Bashcurl -X GET http://localhost:8080/loans/{loanId}
4. [Test] Final Payment (Settlement)Scenario: Make a final payment for the remaining balance (800.00). This triggers the status flip to SETTLED.Bashcurl -i -X POST http://localhost:8080/payments \
-H "Content-Type: application/json" \
-d '{
    "loanId": "{loanId}",
    "paymentAmount": 800.00
}'
5. [Test] Verify SettlementScenario: Confirm the status is now SETTLED and balance is 0.00.Bashcurl -X GET http://localhost:8080/loans/{loanId}
