package com.bancx.sello.loan.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.StringJoiner;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    private String loanId;

    @NotNull
    private BigDecimal loanAmount;

    @NotNull
    private Integer term;

    @NotNull
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Override
    public String toString() {
        return new StringJoiner(", ", "Loan{", "}")
                .add("loanId='" + loanId + "'")
                .add("loanAmount=" + loanAmount)
                .add("term=" + term)
                .add("status=" + status)
                .toString();
    }
}