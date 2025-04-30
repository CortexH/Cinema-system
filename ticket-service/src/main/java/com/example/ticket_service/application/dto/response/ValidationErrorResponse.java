package com.example.ticket_service.application.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String path,
        Map<String, String> messages
) {
}
