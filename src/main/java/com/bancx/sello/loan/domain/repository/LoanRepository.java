package com.bancx.sello.loan.domain.repository;

import com.bancx.sello.loan.domain.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, String> {}