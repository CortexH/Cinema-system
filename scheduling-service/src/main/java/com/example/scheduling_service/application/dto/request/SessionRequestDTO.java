package com.example.scheduling_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record SessionRequestDTO(

        @NotNull(message = "O campo 'movie_id' é obrigatório")
        UUID movie_id,

        @NotNull(message = "O campo 'room_id' é obrigatório")
        UUID room_id,

        @NotNull(message = "O campo 'session_begin_time' é obrigatório")
        String session_begin_time,

        @NotNull(message = "O campo 'session_end_time' é obrigatório")
        String session_end_time,

        @PositiveOrZero(message = "O campo 'setup_duration' deverá ser um número maior ou igual a 0. Representação em segundos")
        @NotNull(message = "O campo 'setup_duration' é obrigatório")
        Long setup_duration,

        @PositiveOrZero(message = "O campo 'movie_duration' deverá ser um número maior ou igual a 0. Representação em segundos")
        Long movie_duration
) {
}
