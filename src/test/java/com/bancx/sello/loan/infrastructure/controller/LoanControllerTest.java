package com.bancx.sello.loan.infrastructure.controller;

import com.bancx.sello.loan.domain.exception.LoanNotFoundException;
import com.bancx.sello.loan.application.LoanService;
import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.domain.model.LoanStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LoanService loanService;

    @Test
    void shouldCreateLoan() throws Exception {
        BigDecimal amount = new BigDecimal("1000.00");
        Integer term = 12;
        Loan mockLoan = new Loan("L1", amount, term, LoanStatus.ACTIVE);

        when(loanService.createLoan(any(BigDecimal.class), any(Integer.class)))
                .thenReturn(mockLoan);

        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loanAmount\": 1000.00, \"term\": 12}"))
                .andExpect(status().isCreated()) // Verify 201 Created
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.loanId").value("L1"))
                .andExpect(jsonPath("$.loanAmount").value(1000.00))
                .andExpect(jsonPath("$.term").value(term))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn404WhenLoanNotFound() throws Exception {
        String loanId = "NON_EXISTENT";
        when(loanService.getLoanById(loanId))
                .thenThrow(new LoanNotFoundException("Loan not found with id: " + loanId));

        mockMvc.perform(get("/loans/" + loanId))
                .andExpect(status().isNotFound())
                // Verifying the ProblemDetail title and type defined in GlobalExceptionHandler
                .andExpect(jsonPath("$.title")
                        .value("Loan Not Found"))
                .andExpect(jsonPath("$.type")
                        .value("https://bancx.sello.com/errors/loan-not-found"));
    }

    //Validation unit tests

    @Test
    void shouldReturn400WhenLoanAmountIsNegative() throws Exception {
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loanAmount\": -500.00, \"term\": 12}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Validation Failed"))
                .andExpect(jsonPath("$.invalid_fields.loanAmount")
                        .value("Loan amount must be greater than zero"));
    }

    @Test
    void shouldReturn400WhenTermIsNull() throws Exception {
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loanAmount\": 1000.00}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalid_fields.term")
                        .value("Term is required"));
    }

    @Test
    void shouldReturn400WhenLoanAmountIsNull() throws Exception {
        mockMvc.perform(post("/loans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"term\": 12}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.invalid_fields.loanAmount")
                        .value("Loan amount is required"));
    }
}