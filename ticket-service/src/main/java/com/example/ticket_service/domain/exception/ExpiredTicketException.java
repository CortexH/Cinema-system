package com.example.ticket_service.domain.exception;

public class ExpiredTicketException extends RuntimeException {
    public ExpiredTicketException(String message) {
        super(message);
    }
}
