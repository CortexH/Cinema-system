package com.example.scheduling_service.domain.exception;

public class SessionStateException extends RuntimeException {
    public SessionStateException(String message) {
        super(message);
    }
}
