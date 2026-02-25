package com.bancx.sello.loan.infrastructure.dto;

import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.domain.model.LoanStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.StringJoiner;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanResponseDTO {

    private String loanId;
    private BigDecimal loanAmount;
    private Integer term;
    private LoanStatus status;

    public static LoanResponseDTO fromEntity(Loan loan) {
        return LoanResponseDTO.builder()
                .loanId(loan.getLoanId())
                .loanAmount(loan.getLoanAmount())
                .term(loan.getTerm())
                .status(loan.getStatus())
                .build();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "LoanResponseDTO{", "}")
                .add("loanId='" + loanId + "'")
                .add("loanAmount=" + loanAmount)
                .add("term=" + term)
                .add("status=" + status)
                .toString();
    }
}