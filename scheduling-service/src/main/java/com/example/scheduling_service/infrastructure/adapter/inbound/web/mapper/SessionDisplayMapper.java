package com.example.scheduling_service.infrastructure.adapter.inbound.web.mapper;

import com.example.scheduling_service.application.dto.response.SessionDisplayDTO;
import com.example.scheduling_service.domain.model.Session;

public class SessionDisplayMapper {

    public static SessionDisplayDTO toDTO(Session session){
        return new SessionDisplayDTO(
                session.getId().value(),
                session.getRoomId().toString(),
                session.getMovieId().toString(),
                session.getSessionBeginTime(),
                session.getSessionEndTime(),
                session.getMovieDuration().toSeconds(),
                session.getSetupTime().toSeconds()
        );
    }

}
