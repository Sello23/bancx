package com.bancx.sello.payment.infrastructure.controller;

import com.bancx.sello.payment.application.PaymentService;
import com.bancx.sello.payment.domain.model.Payment;
import com.bancx.sello.payment.infrastructure.dto.PaymentRequestDTO;
import com.bancx.sello.payment.infrastructure.dto.PaymentResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> process(@Valid @RequestBody PaymentRequestDTO request) {
        Payment payment = paymentService.processPayment(request.getLoanId(), request.getPaymentAmount());
        return new ResponseEntity<>(PaymentResponseDTO.fromEntity(payment), HttpStatus.CREATED);
    }
}