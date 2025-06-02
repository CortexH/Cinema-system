package com.example.scheduling_service.application.dto.response;

import java.util.UUID;

public record SessionDisplayDTO(

        UUID session_id,
        String room_id,
        String movie_name,
        String movie_id,
        String session_display_time,
        String session_end_time

) {
}
