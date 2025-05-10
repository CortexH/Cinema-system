package com.example.ticket_service.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(

        UUID id,

        String qrCode,
        String movie,
        LocalDateTime movie_time,

        String room,
        String seat,

        Boolean valid,
        Boolean in_use,
        Boolean accessibility

) {
}
