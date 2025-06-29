package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity.SessionEntity;

public class SessionMapper {

    public static Session toInbound(SessionEntity entity){
        return new Session(
                SessionIdVO.from(entity.getId()),
                entity.getSessionMovieId(),
                entity.getRoomId(),
                entity.getSessionBeginTime(),
                entity.getSessionEndTime(),
                entity.getSessionScheduleState(),
                entity.getSetupBeginTime(),
                entity.getMovieDuration()

        );
    }

    public static SessionEntity toOutbound(Session session){
        return new SessionEntity(
                session.getId().value(),
                session.getMovieId(),
                session.getSessionBeginTime(),
                session.getSessionEndTime(),
                session.getSessionScheduleState(),
                session.getRoomId(),
                session.getSetupTime(),
                session.getMovieDuration()
        );
    }

}
