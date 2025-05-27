package com.example.movie_service.application.service;

import com.example.movie_service.domain.model.Movie;
import com.example.movie_service.domain.model.OptionalSession;
import com.example.movie_service.domain.port.in.MovieUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
public class MovieService implements MovieUseCase {


    @Override
    public Movie registerMovie(
            String name,
            LocalTime duration,
            LocalTime validUntil,
            int timesPerDay,
            List<OptionalSession> sessions) {

        


        return null;
    }
}
