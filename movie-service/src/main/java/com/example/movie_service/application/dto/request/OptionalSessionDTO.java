package com.example.movie_service.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OptionalSessionDTO(

        @NotBlank(message = "'time' não pode estar vazio!")
        String time,
        String week_day,
        String date

) {

}
