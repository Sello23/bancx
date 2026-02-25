package com.bancx.sello.payment.domain.repository;

import com.bancx.sello.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {}