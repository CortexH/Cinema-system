package com.example.ticket_service.application.dto.internal;

public record GenericErrorDTO(

        String message,
        String error,
        int code

) {
}
