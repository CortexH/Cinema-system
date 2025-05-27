package com.example.movie_service.domain.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Movie {

    private UUID id;

    private String name;
    private LocalTime duration;

    private Boolean valid;

    private LocalDateTime validTime;

    // vezes que o filme deve ser 'assistido' por dia
    private Integer timesPerDay;


    public Movie(UUID id, String name, LocalTime duration, Boolean valid, LocalDateTime validTime, Integer timesPerDay) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.valid = valid;
        this.validTime = validTime;
        this.timesPerDay = timesPerDay;
    }
}
