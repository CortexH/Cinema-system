package com.example.scheduling_service.domain.port.out;

import com.example.scheduling_service.infrastructure.adapter.outbound.web.entity.MovieEntity;

import java.util.List;

public interface MovieGateway {

    List<MovieEntity> getWeekMovies();

}
