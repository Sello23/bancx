package com.bancx.sello.payment.domain.model;

import com.bancx.sello.loan.domain.model.Loan;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.StringJoiner;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private String paymentId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private LocalDateTime paymentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @Override
    public String toString() {
        return new StringJoiner(", ", "Payment{", "}")
                .add("paymentId='" + paymentId + "'")
                .add("amount=" + amount)
                .add("paymentDate=" + paymentDate)
                .toString();
    }
}