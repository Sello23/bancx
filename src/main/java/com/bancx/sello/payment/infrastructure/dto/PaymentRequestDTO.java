package com.bancx.sello.payment.infrastructure.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PaymentRequestDTO {
    @NotBlank(message = "Loan ID is required")
    private String loanId;

    @NotNull(message = "Payment amount is required")
    @Positive(message = "Payment amount must be greater than zero")
    private BigDecimal paymentAmount;
}