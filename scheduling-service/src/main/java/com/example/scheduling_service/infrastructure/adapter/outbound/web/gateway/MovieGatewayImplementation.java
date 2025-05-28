package com.example.scheduling_service.infrastructure.adapter.outbound.web.gateway;

import com.example.scheduling_service.domain.port.out.MovieGateway;
import com.example.scheduling_service.infrastructure.adapter.outbound.web.entity.MovieEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MovieGatewayImplementation implements MovieGateway {

    private final WebClient webClientRoomGateway;

    @Override
    public List<MovieEntity> getWeekMovies() {
        return List.of();
    }
}
