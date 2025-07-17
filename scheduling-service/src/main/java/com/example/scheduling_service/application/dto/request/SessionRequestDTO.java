package com.example.scheduling_service.application.dto.request;

import java.util.UUID;

public record SessionRequestDTO(
        UUID movie_id,
        UUID room_id,
        String session_begin_time,
        String session_end_time,
        Long setup_duration,
        Long movie_duration
) {
}
