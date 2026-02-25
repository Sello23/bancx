package com.bancx.sello.payment.domain.repository;

import com.bancx.sello.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByLoan_LoanId(String loanId);
}