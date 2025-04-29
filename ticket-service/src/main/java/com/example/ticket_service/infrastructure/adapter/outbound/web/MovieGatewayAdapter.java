package com.example.ticket_service.infrastructure.adapter.outbound.web;

import com.example.ticket_service.application.dto.internal.MovieInformationDTO;
import com.example.ticket_service.domain.port.out.MovieGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class MovieGatewayAdapter implements MovieGateway {

    @Override
    public MovieInformationDTO getMovieDataFromRoom(String roomName) {
        return new MovieInformationDTO(
                "Filme dos mocks", LocalDateTime.now().plusHours(1),
                true, 12, "dado mockado",
                false
        );
    }
}
