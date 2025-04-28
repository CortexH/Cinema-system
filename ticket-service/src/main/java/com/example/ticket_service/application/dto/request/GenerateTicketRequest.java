package com.example.ticket_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record GenerateTicketRequest(

        @NotBlank
        String payment_token,

        @NotBlank
        List<String> seat_numbers,

        @NotBlank
        String room
) {
}
