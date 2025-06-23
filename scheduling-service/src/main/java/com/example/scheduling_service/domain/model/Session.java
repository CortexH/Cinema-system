package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.exception.SessionException;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class Session {

    private SessionIdVO id;

    private UUID movieId;

    private UUID roomId;

    private LocalDateTime sessionBeginTime;
    private LocalDateTime sessionEndTime;

    private Duration setupBefore;
    private Duration setupAfter;
    private Duration movieDuration;

    private SessionScheduleState sessionScheduleState;

    public Session(
            SessionIdVO id, UUID movieId,
            UUID roomId, LocalDateTime sessionBeginTime,
            LocalDateTime sessionEndTime,
            SessionScheduleState sessionScheduleState,
            Duration setupBefore, Duration setupAfter,
            Duration movieDuration

    ) {
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.sessionBeginTime = sessionBeginTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionScheduleState = sessionScheduleState;
        this.setupBefore = setupBefore;
        this.setupAfter = setupAfter;
        this.movieDuration = movieDuration;
    }

    public boolean validateSessionEnded(){
        LocalDateTime now = LocalDateTime.now();
        return this.sessionEndTime.isBefore(now);
    }

    public boolean validateSessionBegin(){
        LocalDateTime now = LocalDateTime.now();
        return this.sessionBeginTime.isBefore(now) && this.sessionEndTime.isAfter(now);
    }

    // retorna TRUE se estiver no setup time, retorna FALSE se não estiver
    public boolean validateSetupTime(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = this.sessionBeginTime.plusNanos(this.setupBefore.toNanos());

        return time.isBefore(now);
    }


    // retorna true se houve alguma mudança, retorna false se não houve.
    public boolean changeSessionState(){
        if(validateSessionEnded()){
            if(this.sessionScheduleState == SessionScheduleState.FINISHED) return false;
            this.sessionScheduleState = SessionScheduleState.FINISHED;
            return true;
        }

        if(validateSessionBegin()){
            if(this.sessionScheduleState == SessionScheduleState.NOW_WORKING) return false;
            this.sessionScheduleState = SessionScheduleState.NOW_WORKING;
        }

        if(validateSetupTime()){
            if(this.sessionScheduleState == SessionScheduleState.SETUP_IN_PROGRESS) return false;
            this.sessionScheduleState = SessionScheduleState.SETUP_IN_PROGRESS;
            return true;
        }

        if(this.sessionScheduleState == SessionScheduleState.SCHEDULED) return false;
        this.sessionScheduleState = SessionScheduleState.SCHEDULED;
        return true;
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

    public Duration getSetupBefore() {
        return setupBefore;
    }

    public void setSetupBefore(Duration setupBefore) {
        this.setupBefore = setupBefore;
    }

    public Duration getSetupAfter() {
        return setupAfter;
    }

    public void setSetupAfter(Duration setupAfter) {
        this.setupAfter = setupAfter;
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
}
