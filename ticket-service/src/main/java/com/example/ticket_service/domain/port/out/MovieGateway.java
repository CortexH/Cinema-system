package com.example.ticket_service.domain.port.out;

import com.example.ticket_service.application.dto.internal.MovieInformationDTO;

public interface MovieGateway {

    MovieInformationDTO getMovieDataFromRoom(String roomName);

}
