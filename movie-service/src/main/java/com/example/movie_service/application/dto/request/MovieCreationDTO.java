package com.example.movie_service.application.dto.request;

public record MovieCreationDTO(

        String name,
        String duration,
        String valid_until,

        int timesPerDay,

        OptionalSessionDTO[] optional_sessions

) {
}
