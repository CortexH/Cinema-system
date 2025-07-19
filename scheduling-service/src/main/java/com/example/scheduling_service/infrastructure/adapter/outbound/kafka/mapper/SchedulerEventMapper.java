package com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper;

import br.com.cinemaSYS.events.scheduler.SchedulerEvent;
import br.com.cinemaSYS.events.scheduler.SchedulerEventType;
import br.com.cinemaSYS.events.scheduler.SessionDTO;
import com.example.scheduling_service.domain.domainEvents.*;

import java.time.Instant;
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
                        .setMovieId(event.movieId().toString())
                        .setRoomId(event.roomId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(event.sessionEndTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setSessionBeginTime(event.sessionBeginTime().atZone(ZoneId.of("America/Sao_Paulo")).toInstant())
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }

}
