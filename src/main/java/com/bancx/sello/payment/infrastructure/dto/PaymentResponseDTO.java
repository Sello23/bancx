package com.bancx.sello.payment.infrastructure.dto;

import com.bancx.sello.payment.domain.model.Payment;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private String paymentId;
    private String loanId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;

    public static PaymentResponseDTO fromEntity(Payment payment) {
        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .loanId(payment.getLoan().getLoanId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "PaymentResponseDTO{", "}")
                .add("paymentId='" + paymentId + "'")
                .add("loanId='" + loanId + "'")
                .add("amount=" + amount)
                .add("paymentDate=" + paymentDate)
                .toString();
    }
}