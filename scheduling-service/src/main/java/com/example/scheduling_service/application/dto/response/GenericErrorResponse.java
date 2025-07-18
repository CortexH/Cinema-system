package com.example.scheduling_service.application.dto.response;

import java.time.LocalDateTime;

public record GenericErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
