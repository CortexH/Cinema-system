package com.example.movie_service.infrastructure.adapter.inbound.web.controller;

import com.example.movie_service.application.dto.request.MovieCreationDTO;
import com.example.movie_service.domain.model.Movie;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQueries;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @PostMapping
    public ResponseEntity<?> registerMovie(@RequestBody @Valid MovieCreationDTO data){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try{

            LocalTime duration = LocalTime.parse(data.duration(), timeFormatter);
            LocalDateTime validUntil = LocalDateTime.parse(data.valid_until(), dateTimeFormatter);

            


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<?> findMovies(
            @PathVariable(value = "movieId", required = false) String movieId
    ){
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable("movieId") String movieId){
        return ResponseEntity.noContent().build();
    }

}
