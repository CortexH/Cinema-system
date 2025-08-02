package com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper;

import br.com.cinemaSYS.events.scheduler.*;
import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.domain.enums.SessionEventType;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class SchedulerEventMapper {

    public static SchedulerEvent fromSessionEnd(SessionEndEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_ENDED)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

    public static SchedulerEvent fromSessionBegin(SessionBeginEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_BEGIN)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

    public static SchedulerEvent fromSessionNearToBegin(SessionNearToBeginEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_NEAR_TO_BEGIN)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

    public static SchedulerEvent fromSessionScheduled(SessionScheduledEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_ADDED)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

    public static SchedulerEvent fromSessionSetupBegin(SessionSetupBeginEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_BEGIN)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

    public static SchedulerEvent fromSessionRemoved(SessionRemovedEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_REMOVED)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setSessionId(event.sessionId().toString())
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

    public static SchedulerEvent fromSessionChanged(SessionChangedEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_EDITED)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .setNewSession(ChangedSession.newBuilder()
                        .setMovieId(event.newSession().getMovieId().toString())
                        .setRoomId(event.newSession().getRoomId().toString())
                        .setSessionId(event.newSession().getId().value().toString())
                        .setSessionEndTime(event.newSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.newSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.newSession().getMovieDuration().toMillis())
                        .setRemoved(event.newSession().isRemoved())
                        .setSessionState(stateToEvent(event.newSession().getSessionScheduleState()))
                        .setSetupDuration(event.newSession().getSetupTime().toMillis())
                        .build())
                .build();
    }

    public static sessionScheduledState stateToEvent(SessionScheduleState state){
        if(state == null) return null;
        return switch (state){
            case SCHEDULED -> sessionScheduledState.SCHEDULED;
            case SETUP_IN_PROGRESS -> sessionScheduledState.SETUP_IN_PROGRESS;
            case NOW_WORKING -> sessionScheduledState.NOW_WORKING;
            case FINISHED -> sessionScheduledState.FINISHED;
        };
    }


}
