package com.example.ticket_service.domain.exception;

public class ReserveValidationFailedException extends RuntimeException {
    public ReserveValidationFailedException(String message) {
        super(message);
    }
}
