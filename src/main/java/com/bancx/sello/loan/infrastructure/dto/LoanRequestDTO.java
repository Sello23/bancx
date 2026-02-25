package com.bancx.sello.loan.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRequestDTO {

    @NotNull(message = "Loan amount is required")
    @Positive(message = "Loan amount must be greater than zero")
    private BigDecimal loanAmount;

    @NotNull(message = "Term is required")
    @Positive(message = "Term must be at least 1 month")
    private Integer term;

    @Override
    public String toString() {
        return new StringJoiner(", ", "LoanRequestDTO{", "}")
                .add("loanAmount=" + loanAmount)
                .add("term=" + term)
                .toString();
    }
}