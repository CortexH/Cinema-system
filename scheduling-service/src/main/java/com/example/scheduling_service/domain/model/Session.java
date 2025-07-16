package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.domain.enums.SessionEventType;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.exception.SessionException;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Session {

    private final List<SessionEvent> events = new ArrayList<>();

    private final SessionIdVO id;
    private final UUID movieId;
    private final UUID roomId;

    private LocalDateTime sessionBeginTime; // data e hora que a sessão inicia (contado logo após o início do 'setupTime')
    private LocalDateTime sessionEndTime;

    private Duration setupTime; // tempo inicial antes do filme começar (limpeza ou coisa parecida)
    private Duration movieDuration;

    private SessionScheduleState sessionScheduleState;

    public Session(
            SessionIdVO id, UUID movieId,
            UUID roomId, LocalDateTime sessionBeginTime,
            LocalDateTime sessionEndTime,
            SessionScheduleState sessionScheduleState,
            Duration setupTime, Duration movieDuration
    ) {
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.sessionBeginTime = sessionBeginTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionScheduleState = sessionScheduleState;
        this.setupTime = setupTime;
        this.movieDuration = movieDuration;
        this.events.add(createSessionScheduledEvent());
    }

    public Session(
            SessionIdVO id, UUID movieId,
            UUID roomId, LocalDateTime sessionBeginTime,
            LocalDateTime sessionEndTime,
            Duration setupTime, Duration movieDuration
    ){
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.sessionBeginTime = sessionBeginTime;
        this.sessionEndTime = sessionEndTime;
        this.setupTime = setupTime;
        this.movieDuration = movieDuration;
    }

    public boolean syncStateWithLocalTime(){
        boolean validated = false;
        while (validateSession()) {
            validated = true;
        }
        return validated;
    }

    private boolean validateSession(){
        SessionScheduleState state = this.sessionScheduleState;

        switch (state){
            case SCHEDULED -> {
                if(hasSessionPeriodBegun()){
                    this.sessionScheduleState = SessionScheduleState.SETUP_IN_PROGRESS;
                    this.events.add(createSessionSetupBeginEvent());
                    return true;
                }
                return false;
            }
            case SETUP_IN_PROGRESS -> {
                if(hasSetupPeriodFinished()){
                    this.sessionScheduleState = SessionScheduleState.NOW_WORKING;
                    this.events.add(createSessionBeginEvent());
                    return true;
                }
                return false;
            }

            case NOW_WORKING -> {
                if(hasSessionPeriodFinished()){
                    this.sessionScheduleState = SessionScheduleState.FINISHED;
                    this.events.add(createSessionEndEvent());
                    return true;
                }
                return false;
            }
            case FINISHED -> {
                return false;
            }
            case null -> throw new SessionException("'sessionScheduledState' está como 'nulo'");
        }
    }

    private SessionSetupBeginEvent createSessionSetupBeginEvent(){
        return new SessionSetupBeginEvent(
                SessionEventType.SESSION_SETUP, Instant.now(),
                id.value(), movieId, roomId, sessionBeginTime,
                sessionEndTime, movieDuration

        );
    }

    private SessionScheduledEvent createSessionScheduledEvent(){
        return new SessionScheduledEvent(
                SessionEventType.SESSION_ADDED, Instant.now(),
                id.value(), movieId, roomId, sessionBeginTime,
                sessionEndTime, movieDuration
        );
    }

    private SessionBeginEvent createSessionBeginEvent(){
        return new SessionBeginEvent(
                SessionEventType.SESSION_BEGIN, Instant.now(),
                this.id.value(), movieId, roomId,
                sessionBeginTime, sessionEndTime,
                movieDuration
        );
    }

    private SessionEndEvent createSessionEndEvent(){
        return new SessionEndEvent(
                SessionEventType.SESSION_ENDED, Instant.now(),
                id.value(), movieId, roomId, sessionBeginTime,
                sessionEndTime, movieDuration
        );
    }

    // VALIDATIONS

    private boolean hasSessionPeriodBegun() {
        return LocalDateTime.now().isAfter(this.sessionBeginTime);
    }

    private boolean hasSetupPeriodFinished() {
        return LocalDateTime.now().isAfter(this.sessionBeginTime.plusNanos(this.setupTime.toNanos()));
    }

    private boolean hasSessionPeriodFinished() {
        return LocalDateTime.now().isAfter(this.sessionEndTime);
    }

    // get / set

    public List<SessionEvent> pullDomainEvents(){
        if(this.events.isEmpty()){
            return Collections.emptyList();
        }

        List<SessionEvent> pulledEvents = new ArrayList<>(this.events);
        this.events.clear();
        return pulledEvents;
    }

    public SessionIdVO getId() {
        return id;
    }

    public UUID getMovieId() {
        return movieId;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public LocalDateTime getSessionBeginTime() {
        return sessionBeginTime;
    }

    public void setSessionBeginTime(LocalDateTime sessionBeginTime) {
        this.sessionBeginTime = sessionBeginTime;
    }

    public LocalDateTime getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(LocalDateTime sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public Duration getSetupTime() {
        return setupTime;
    }

    public void setSetupTime(Duration setupTime) {
        this.setupTime = setupTime;
    }

    public SessionScheduleState getSessionScheduleState() {
        return sessionScheduleState;
    }

    public void setSessionScheduleState(SessionScheduleState sessionScheduleState) {
        this.sessionScheduleState = sessionScheduleState;
    }

    public Duration getMovieDuration() {
        return movieDuration;
    }

    public void setMovieDuration(Duration movieDuration) {
        this.movieDuration = movieDuration;
    }

    @Override
    public String toString() {
        return "Session{" +
                "events=" + events +
                ", id=" + id +
                ", movieId=" + movieId +
                ", roomId=" + roomId +
                ", sessionBeginTime=" + sessionBeginTime +
                ", sessionEndTime=" + sessionEndTime +
                ", setupTime=" + setupTime +
                ", movieDuration=" + movieDuration +
                ", sessionScheduleState=" + sessionScheduleState +
                '}';
    }
}
