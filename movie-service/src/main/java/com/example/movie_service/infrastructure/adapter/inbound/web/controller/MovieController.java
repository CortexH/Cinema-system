package com.example.movie_service.infrastructure.adapter.inbound.web.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @PostMapping
    public ResponseEntity<?> registerMovie(){
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
