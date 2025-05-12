package com.example.movie_service.application.dto.request;

import com.example.movie_service.domain.enums.SessionWeekday;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExclusiveSessionDTO(

        @NotBlank(message = "'time' não pode estar vazio!")
        String time,


        String week_day,


        String date

) {

}
