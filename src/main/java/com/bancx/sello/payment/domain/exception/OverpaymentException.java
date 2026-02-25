package com.bancx.sello.payment.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OverpaymentException extends RuntimeException {
    public OverpaymentException(String message) {
        super(message);
    }
}