package com.bancx.sello.payment.controller;

import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.payment.application.PaymentService;
import com.bancx.sello.payment.domain.exception.OverpaymentException;
import com.bancx.sello.payment.domain.model.Payment;
import com.bancx.sello.payment.infrastructure.controller.PaymentController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PaymentService paymentService;

    @Test
    void shouldProcessPaymentSuccessfully() throws Exception {
        String loanId = "L1";
        Loan loan = Loan.builder().loanId(loanId).build();
        Payment mockPayment = Payment.builder()
                .paymentId("P1")
                .amount(new BigDecimal("200.00"))
                .paymentDate(LocalDateTime.now())
                .loan(loan)
                .build();

        when(paymentService.processPayment(eq(loanId), any(BigDecimal.class))).thenReturn(mockPayment);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loanId\": \"L1\", \"paymentAmount\": 200.00}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value("P1"))
                .andExpect(jsonPath("$.loanId").value("L1"));
    }

    @Test
    void shouldReturn409OnOverpayment() throws Exception {
        when(paymentService.processPayment(any(), any()))
                .thenThrow(new OverpaymentException("Overpayment detected"));

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"loanId\": \"L1\", \"paymentAmount\": 5000.00}"))
                .andExpect(status().isConflict()) // 409
                .andExpect(jsonPath("$.title").value("Overpayment Error"))
                .andExpect(jsonPath("$.detail").value("Overpayment detected"))
                .andExpect(jsonPath("$.type").value("https://bancx.sello.com/errors/overpayment-detected"));
    }
}