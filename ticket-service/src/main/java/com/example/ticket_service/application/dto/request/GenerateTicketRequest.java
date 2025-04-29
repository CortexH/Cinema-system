package com.example.ticket_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GenerateTicketRequest(

        @NotNull
        String payment_token,

        @NotEmpty
        List<String> seat_numbers,

        @NotNull
        String room
) {
}
