package com.example.movie_service.domain.model;

import com.example.movie_service.domain.enums.SessionWeekday;

import java.time.LocalDate;
import java.time.LocalTime;

public class OptionalSession {

    private LocalTime time;
    private SessionWeekday weekDay;

    private Movie movie;

    private LocalDate date;

}
