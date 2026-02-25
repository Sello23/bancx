package com.bancx.sello.loan.application;

import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.domain.model.LoanStatus;
import com.bancx.sello.loan.domain.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldCreateLoanSuccessfully() {
        BigDecimal amount = new BigDecimal("1000.00");
        Loan mockLoan = new Loan("L1", amount, 12, LoanStatus.ACTIVE);
        when(loanRepository.save(any(Loan.class))).thenReturn(mockLoan);

        Loan result = loanService.createLoan(amount, 12);

        assertNotNull(result);
        assertEquals(amount, result.getLoanAmount());
        assertEquals(LoanStatus.ACTIVE, result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenLoanNotFound() {
        String loanId = "NON_EXISTENT";
        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> loanService.getLoanById(loanId));
    }
}