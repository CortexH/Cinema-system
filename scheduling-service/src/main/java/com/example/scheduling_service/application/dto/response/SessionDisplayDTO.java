package com.example.scheduling_service.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record SessionDisplayDTO(

        UUID session_id,
        String room_id,
        String movie_id,
        LocalDateTime session_begin_time,
        LocalDateTime session_end_time,
        Long movie_duration
) {
}
