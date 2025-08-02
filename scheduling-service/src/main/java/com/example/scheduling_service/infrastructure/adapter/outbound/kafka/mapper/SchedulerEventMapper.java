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

    public static SessionEvent toSessionEvent(SchedulerEvent event){
        return switch (event.getEventType()){
            case SESSION_ADDED -> new SessionScheduledEvent(
                    SessionEventType.SESSION_ADDED, event.getTimestamp(),
                    UUID.fromString(event.getSession().getSessionId()),
                    UUID.fromString(event.getSession().getMovieId()),
                    UUID.fromString(event.getSession().getRoomId()),
                    (event.getSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                    event.getSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
                    Duration.ofMillis(event.getSession().getMovieDuration())
            );
            case SESSION_NEAR_TO_BEGIN -> new SessionNearToBeginEvent(
                    SessionEventType.SESSION_NEAR_TO_BEGIN, event.getTimestamp(),
                    UUID.fromString(event.getSession().getSessionId()),
                    UUID.fromString(event.getSession().getMovieId()),
                    UUID.fromString(event.getSession().getRoomId()),
                    (event.getSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                    event.getSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
                    Duration.ofMillis(event.getSession().getMovieDuration())
            );
            case SESSION_BEGIN -> new SessionBeginEvent(
                    SessionEventType.SESSION_BEGIN, event.getTimestamp(),
                    UUID.fromString(event.getSession().getSessionId()),
                    UUID.fromString(event.getSession().getMovieId()),
                    UUID.fromString(event.getSession().getRoomId()),
                    (event.getSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                    event.getSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
                    Duration.ofMillis(event.getSession().getMovieDuration())
            );
            case SESSION_ENDED -> new SessionEndEvent(
                    SessionEventType.SESSION_ENDED, event.getTimestamp(),
                    UUID.fromString(event.getSession().getSessionId()),
                    UUID.fromString(event.getSession().getMovieId()),
                    UUID.fromString(event.getSession().getRoomId()),
                    (event.getSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                    event.getSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
                    Duration.ofMillis(event.getSession().getMovieDuration())
            );
            case SESSION_EDITED -> new SessionChangedEvent(
                    SessionEventType.SESSION_EDITED, event.getTimestamp(),
                    UUID.fromString(event.getSession().getSessionId()),
                    UUID.fromString(event.getSession().getMovieId()),
                    UUID.fromString(event.getSession().getRoomId()),
                    (event.getSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                    event.getSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
                    Duration.ofMillis(event.getSession().getMovieDuration()),
                    new Session(
                            SessionIdVO.from(event.getNewSession().getSessionId()),
                            UUID.fromString(event.getNewSession().getMovieId()),
                            UUID.fromString(event.getNewSession().getRoomId()),
                            (event.getNewSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                            (event.getNewSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                            Duration.ofMillis(event.getNewSession().getSetupDuration()),
                            Duration.ofMillis(event.getNewSession().getMovieDuration()),
                            stateFromEvent(event.getNewSession().getSessionState()),
                            event.getNewSession().getRemoved()
                    )
            );
            case SESSION_REMOVED -> new SessionRemovedEvent(
                    SessionEventType.SESSION_REMOVED, event.getTimestamp(),
                    UUID.fromString(event.getSession().getSessionId()),
                    UUID.fromString(event.getSession().getMovieId()),
                    UUID.fromString(event.getSession().getRoomId()),
                    (event.getSession().getSessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime()),
                    event.getSession().getSessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toLocalDateTime(),
                    Duration.ofMillis(event.getSession().getMovieDuration())
            );
        };
    }

    public static SessionScheduleState stateFromEvent(sessionScheduledState state){
        if(state == null) return null;
        return switch (state){
            case PENDING -> null;
            case SCHEDULED -> SessionScheduleState.SCHEDULED;
            case SETUP_IN_PROGRESS -> SessionScheduleState.SETUP_IN_PROGRESS;
            case NOW_WORKING -> SessionScheduleState.NOW_WORKING;
            case FINISHED -> SessionScheduleState.FINISHED;
        };
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
