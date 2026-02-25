package com.bancx.sello.integration;

import com.bancx.sello.loan.infrastructure.dto.LoanRequestDTO;
import com.bancx.sello.loan.infrastructure.dto.LoanResponseDTO;
import com.bancx.sello.payment.infrastructure.dto.PaymentRequestDTO;
import com.bancx.sello.payment.infrastructure.dto.PaymentResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanPaymentIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCompleteFullLoanLifecycle() {
        // 1. Create a Loan
        LoanRequestDTO loanRequest = new LoanRequestDTO(new BigDecimal("1000.00"), 12);
        ResponseEntity<LoanResponseDTO> loanResponse = restTemplate.postForEntity("/loans", loanRequest, LoanResponseDTO.class);

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());
        assertNotNull(loanResponse.getBody());
        String loanId = loanResponse.getBody().getLoanId();
        assertNotNull(loanId);

        // 2. Process First Payment (400.00)
        PaymentRequestDTO payment1 = new PaymentRequestDTO(loanId, new BigDecimal("400.00"));
        ResponseEntity<PaymentResponseDTO> payResp1 = restTemplate.postForEntity("/payments", payment1, PaymentResponseDTO.class);
        assertEquals(HttpStatus.CREATED, payResp1.getStatusCode());

        // 3. Process Second Payment (600.00) - Should settle the loan
        PaymentRequestDTO payment2 = new PaymentRequestDTO(loanId, new BigDecimal("600.00"));
        restTemplate.postForEntity("/payments", payment2, PaymentResponseDTO.class);

        // 4. Verify Loan is SETTLED and balance is 0
        ResponseEntity<LoanResponseDTO> updatedLoan = restTemplate.getForEntity("/loans/" + loanId, LoanResponseDTO.class);
        assertNotNull(updatedLoan.getBody());
        assertEquals(new BigDecimal("0.00"), updatedLoan.getBody().getLoanAmount());
        assertEquals("SETTLED", updatedLoan.getBody().getStatus().toString());

        // 5. Check Payment History
        ResponseEntity<List<PaymentResponseDTO>> historyResponse = restTemplate.exchange(
                "/payments/loan/" + loanId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PaymentResponseDTO>>() {
                }
        );

        assertEquals(HttpStatus.OK, historyResponse.getStatusCode());
        List<PaymentResponseDTO> history = historyResponse.getBody();
        assertNotNull(history);
        assertEquals(2, history.size());
        assertTrue(history.stream().anyMatch(p -> p.getAmount().compareTo(new BigDecimal("400.00")) == 0));
        assertTrue(history.stream().anyMatch(p -> p.getAmount().compareTo(new BigDecimal("600.00")) == 0));
    }
}