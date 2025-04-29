package com.example.ticket_service.application.dto.response;

public record TicketConciliationResponse(
        String seat_number,
        String room_number
) {
}
