package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.exception.MovieException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

public class Movie {

    private UUID movieId;
    private String movieName;

    private Duration duration;
    private LocalDateTime validUntil;

    private Integer timesPerDay;

    private Duration setupTime;
    private Duration finishTime;

    public Movie(
            UUID movieId, String movieName,
            Duration duration, LocalDateTime validUntil,
            Integer timesPerDay, Duration cinemaOpenTime,
            Duration setupTime, Duration finishTime
    ){

        this.movieId = movieId;
        this.movieName = movieName;
        this.duration = duration;
        this.validUntil = validUntil;
        this.timesPerDay = timesPerDay;
        this.setupTime = setupTime;
        this.finishTime = finishTime;

        validateMovieTimesPerDay(cinemaOpenTime);
    }

    private void validateMovieTimesPerDay(Duration openTime){
        Duration todayTotalDuration = duration
                .plus(setupTime)
                .plus(finishTime)
                .multipliedBy(Long.valueOf(timesPerDay));

        if(LocalDateTime.now().plus(todayTotalDuration).isAfter(this.validUntil)){
            throw new MovieException("O filme será exibido após a validade");
        }

        if((this.duration.toSeconds() *
                (timesPerDay + setupTime.toSeconds() + finishTime.toSeconds())
                ) > openTime.toSeconds()){

            throw new MovieException("O filme será exibido durante mais tempo que o cinema estará aberto.");
        }
    }



}
