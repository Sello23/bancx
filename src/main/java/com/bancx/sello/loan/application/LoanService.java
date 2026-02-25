package com.bancx.sello.loan.application;

import com.bancx.sello.infrastructure.exception.LoanNotFoundException;
import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.domain.model.LoanStatus;
import com.bancx.sello.loan.domain.repository.LoanRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class LoanService {
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan createLoan(BigDecimal amount, Integer term) {
        Loan loan = new Loan(UUID.randomUUID().toString(), amount, term, LoanStatus.ACTIVE);
        return loanRepository.save(loan);
    }

    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));
    }

}