package com.bancx.sello.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import io.micrometer.common.lang.NonNullApi;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles 404 Not Found when a loan is missing.
     */
    @ExceptionHandler(LoanNotFoundException.class)
    public ProblemDetail handleLoanNotFound(LoanNotFoundException ex) {
        return createProblemDetail(HttpStatus.NOT_FOUND, "Loan Not Found", ex.getMessage(), "loan-not-found");
    }

    /**
     * Catch-all for any other unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Server Error");
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @Override
    protected org.springframework.http.ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problemDetail = createProblemDetail(HttpStatus.BAD_REQUEST, "Validation Failed", "One or more fields are invalid.", "validation-error");
        problemDetail.setProperty("invalid_fields", fieldErrors);

        return status(status).body(problemDetail);
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail, String errorCode) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        // Updated URI to match the bancx project domain
        problemDetail.setType(URI.create("https://bancx.sello.com/errors/" + errorCode));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }
}