package com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper;


import com.example.scheduling_service.application.dto.internal.SessionDTO;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.ZoneOffset;

public class SessionEventDTOMapper {

    public static SessionDTO toDTO(Session session){
        return new SessionDTO(
                session.getId().value(), session.getMovieId(),
                session.getRoomId(), session.getSessionBeginTime(),
                session.getSessionEndTime(), session.getMovieDuration()
        );
    }

}
