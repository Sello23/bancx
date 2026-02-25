package com.bancx.sello.payment.application;

import com.bancx.sello.loan.application.LoanService;
import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.domain.model.LoanStatus;
import com.bancx.sello.payment.domain.exception.OverpaymentException;
import com.bancx.sello.payment.domain.model.Payment;
import com.bancx.sello.payment.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final LoanService loanService;

    @Transactional
    public Payment processPayment(String loanId, BigDecimal amount) {
        Loan loan = loanService.getLoanById(loanId);

        if (amount.compareTo(loan.getLoanAmount()) > 0) {
            throw new OverpaymentException("Payment amount " + amount + " exceeds remaining balance " + loan.getLoanAmount());
        }

        loan.setLoanAmount(loan.getLoanAmount().subtract(amount));
        if (loan.getLoanAmount().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.SETTLED);
        }

        Payment payment = Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .amount(amount)
                .paymentDate(LocalDateTime.now())
                .loan(loan)
                .build();

        return paymentRepository.save(payment);
    }
}