package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper;

import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.entity.SessionEditCommandEntity;

import java.time.LocalDateTime;

public class SessionEditCommandMapper {

    public static SessionEditCommandEntity toOutbound(SessionEditCommand entity){
        return new SessionEditCommandEntity(
                entity.id(), entity.relatedSessionId(),
                entity.movieId(), entity.roomId(),
                entity.sessionBeginTime(), entity.sessionEndTime(),
                entity.setupTime(), entity.movieDuration(), entity.createdAt(),
                entity.processedAt(), entity.status(), entity.sessionScheduleState()
        );
    }

    public static SessionEditCommand toInbound(SessionEditCommandEntity entity){
        return new SessionEditCommand(
                entity.getId(), entity.getRelatedSession(),
                entity.getNewMovieId(), entity.getNewRoomId(),
                entity.getNewBeginTime(), entity.getNewEndTime(),
                entity.getSetupTime(), entity.getMovieDuration(), entity.getSessionState(),
                entity.getProcessedAt(), entity.getCreatedAt(), entity.getStatus()
        );
    }


}
