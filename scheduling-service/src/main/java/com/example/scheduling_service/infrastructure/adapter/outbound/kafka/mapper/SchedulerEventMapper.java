package com.example.scheduling_service.infrastructure.adapter.outbound.kafka.mapper;

import br.com.cinemaSYS.events.scheduler.SchedulerEvent;
import br.com.cinemaSYS.events.scheduler.SchedulerEventType;
import br.com.cinemaSYS.events.scheduler.SessionDTO;
import com.example.scheduling_service.domain.domainEvents.*;

import java.time.Instant;
import java.util.UUID;

public class SchedulerEventMapper {

    public static SchedulerEvent fromSessionEnd(SessionEndEvent event){
        return SchedulerEvent.newBuilder()
                .setEventId(UUID.randomUUID().toString())
                .setEventType(SchedulerEventType.SESSION_ENDED)
                .setTimestamp(event.timestamp())
                .setSession(SessionDTO.newBuilder()
                        .setMovieId(event.movieId().toString())
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(Instant.from(event.sessionEndTime()))
                        .setSessionBeginTime(Instant.from(event.sessionEndTime()))
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
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(Instant.from(event.sessionEndTime()))
                        .setSessionBeginTime(Instant.from(event.sessionEndTime()))
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
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(Instant.from(event.sessionEndTime()))
                        .setSessionBeginTime(Instant.from(event.sessionEndTime()))
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
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(Instant.from(event.sessionEndTime()))
                        .setSessionBeginTime(Instant.from(event.sessionEndTime()))
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
                        .setSessionId(event.sessionId().toString())
                        .setSessionEndTime(Instant.from(event.sessionEndTime()))
                        .setSessionBeginTime(Instant.from(event.sessionEndTime()))
                        .setMovieDuration(event.movieDuration().toMillis())
                        .build())
                .build();
    }



}
