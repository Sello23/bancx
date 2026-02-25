package com.bancx.sello.payment.application;

import com.bancx.sello.loan.application.LoanService;
import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.domain.model.LoanStatus;
import com.bancx.sello.payment.domain.model.Payment;
import com.bancx.sello.payment.domain.repository.PaymentRepository;
import com.bancx.sello.payment.domain.exception.OverpaymentException;
import com.bancx.sello.payment.infrastructure.dto.PaymentResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private LoanService loanService;
    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldProcessPaymentAndReduceBalance() {
        String loanId = "L1";
        Loan loan = new Loan(loanId, new BigDecimal("1000.00"), 12, LoanStatus.ACTIVE);
        when(loanService.getLoanById(loanId)).thenReturn(loan);

        paymentService.processPayment(loanId, new BigDecimal("200.00"));

        assertEquals(new BigDecimal("800.00"), loan.getLoanAmount());
        verify(paymentRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionOnOverpayment() {
        String loanId = "L1";
        Loan loan = new Loan(loanId, new BigDecimal("100.00"), 12, LoanStatus.ACTIVE);
        when(loanService.getLoanById(loanId)).thenReturn(loan);

        assertThrows(OverpaymentException.class, () ->
                paymentService.processPayment(loanId, new BigDecimal("150.00"))
        );
    }

    @Test
    void shouldSettleLoanWhenPaidInFull() {
        String loanId = "L1";
        Loan loan = new Loan(loanId, new BigDecimal("500.00"), 12, LoanStatus.ACTIVE);
        when(loanService.getLoanById(loanId)).thenReturn(loan);

        paymentService.processPayment(loanId, new BigDecimal("500.00"));

        assertEquals(new BigDecimal("0.00"), loan.getLoanAmount());
        assertEquals(LoanStatus.SETTLED, loan.getStatus());
    }

    @Test
    void shouldProcessPaymentSuccessfully() {
        String loanId = "L1";
        BigDecimal amount = new BigDecimal("200.00");
        Loan loan = new Loan(loanId, new BigDecimal("1000.00"), 12, LoanStatus.ACTIVE);

        when(loanService.getLoanById(loanId)).thenReturn(loan);
        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.processPayment(loanId, amount);

        assertNotNull(result.getPaymentId());
        assertEquals(amount, result.getAmount());
        assertEquals(new BigDecimal("800.00"), loan.getLoanAmount());
    }

    @Test
    void shouldReturnPaymentHistoryForLoan() {
        String loanId = "L1";
        Loan loan = Loan.builder().loanId(loanId).build();
        Payment p1 = Payment.builder().paymentId("P1").loan(loan).build();
        Payment p2 = Payment.builder().paymentId("P2").loan(loan).build();

        when(paymentRepository.findByLoan_LoanId(loanId)).thenReturn(java.util.List.of(p1, p2));

        List<PaymentResponseDTO> history = paymentService.getPaymentHistory(loanId);

        assertEquals(2, history.size());
        assertEquals("P1", history.get(0).getPaymentId());
        assertEquals("P2", history.get(1).getPaymentId());
    }
}