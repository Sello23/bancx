package com.bancx.sello.loan.infrastructure.controller;

import com.bancx.sello.loan.application.LoanService;
import com.bancx.sello.loan.domain.model.Loan;
import com.bancx.sello.loan.infrastructure.dto.LoanRequestDTO;
import com.bancx.sello.loan.infrastructure.dto.LoanResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> create(@Valid @RequestBody LoanRequestDTO request) {
        Loan loan = loanService.createLoan(request.getLoanAmount(), request.getTerm());
        return new ResponseEntity<>(LoanResponseDTO.fromEntity(loan), HttpStatus.CREATED);
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<LoanResponseDTO> getLoan(@PathVariable String loanId) {
        Loan loan = loanService.getLoanById(loanId);
        return ResponseEntity.ok(LoanResponseDTO.fromEntity(loan));
    }
}