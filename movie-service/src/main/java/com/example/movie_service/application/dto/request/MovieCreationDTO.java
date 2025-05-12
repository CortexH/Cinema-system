package com.example.movie_service.application.dto.request;

public record MovieCreationDTO(

        String name,
        String duration,
        String valid_until,

        String timesPerDay,

        String[] exclusive_times

) {
}
