package com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper;

import br.com.cinemaSYS.events.scheduler.SessionDTO;
import com.example.scheduling_service.domain.model.Session;

import java.time.ZoneOffset;

public class SessionEventDTOMapper {

    public static SessionDTO toDTO(com.example.scheduling_service.application.dto.internal.SessionDTO session){
        return SessionDTO.newBuilder()
                .setRoomId(session.roomId().toString())
                .setSessionId(session.sessionId().toString())
                .setSessionBeginTime(session.sessionBeginTime().toInstant(ZoneOffset.UTC))
                .setSessionEndTime(session.sessionEndTime().toInstant(ZoneOffset.UTC))
                .setMovieId(session.movieId().toString())
                .setMovieDuration(session.movieDuration().toMillis())
                .build();
    }

}
