# BancX Loan Management System

A professional, TDD-driven Spring Boot application for managing loan lifecycles and payment processing. This project 
demonstrates Clean Architecture principles, Domain-Driven Design (DDD), and robust error handling using RFC 7807.

---

## ## Features

* **Loan Management**: Create and retrieve loans with automated status tracking (ACTIVE, SETTLED).
* **Payment Processing**: Real-time balance reduction with atomic transaction management.
* **Validation**: Strict input validation using Jakarta Bean Validation.
* **Error Handling**: Centralized exception management returning standardized `ProblemDetail` responses.
* **Persistence**: Fast, in-memory H2 database for rapid development and testing.

---

## ## Tech Stack

* **Java 21**: Leveraging modern language features like Records and simplified Stream APIs.
* **Spring Boot 3.x**: Core framework for dependency injection and REST services.
* **Spring Data JPA**: For abstraction over the H2 database.
* **Lombok**: To reduce boilerplate code in DTOs and Entities.
* **JUnit 5 & Mockito**: Driving the development through Test-Driven Development (TDD).

---

## ## Getting Started

### ### Prerequisites
* JDK 21 or higher
* Gradle 8.x

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