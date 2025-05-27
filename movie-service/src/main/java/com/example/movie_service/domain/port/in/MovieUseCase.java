package com.example.movie_service.domain.port.in;

import com.example.movie_service.domain.model.Movie;
import com.example.movie_service.domain.model.OptionalSession;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MovieUseCase {

    Movie registerMovie(String name, LocalTime duration, LocalTime validUntil, int timesPerDay, List<OptionalSession> sessions);


}
