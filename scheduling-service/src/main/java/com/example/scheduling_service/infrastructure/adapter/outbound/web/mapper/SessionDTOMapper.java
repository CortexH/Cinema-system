package com.example.scheduling_service.infrastructure.adapter.outbound.web.mapper;

import com.example.scheduling_service.application.dto.internal.SessionDTO;
import com.example.scheduling_service.domain.model.Session;

public class SessionDTOMapper {

    public static SessionDTO toDto(Session session){
        return new SessionDTO(
                session.getId().value(),
                session.getMovieId(),
                session.getRoomId(),
                session.getSessionBeginTime(),
                session.getSessionEndTime(),
                session.getMovieDuration()
        );
    }

}
