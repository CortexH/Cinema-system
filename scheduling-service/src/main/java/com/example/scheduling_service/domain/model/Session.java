package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.domainEvents.*;
import com.example.scheduling_service.domain.enums.SessionEventType;
import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.exception.SessionConflictException;
import com.example.scheduling_service.domain.exception.SessionException;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
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
        calculateMovieDurationIfNull();
        calculateSetupDuration();
        validateData();
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
        this.sessionScheduleState = SessionScheduleState.SCHEDULED;
        calculateMovieDurationIfNull();
        calculateSetupDuration();
        validateData();
        this.events.add(createSessionScheduledEvent());
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

    private void calculateMovieDurationIfNull(){
        if(this.movieDuration != null && this.movieDuration != Duration.ZERO) return;
        if(this.setupTime == null) this.setupTime = Duration.ZERO;

        this.movieDuration = Duration.between(
                sessionBeginTime.plusNanos(setupTime.toNanos()),
                sessionEndTime
        );
    }

    private void calculateSetupDuration(){
        this.setupTime = Duration.between(sessionBeginTime.plus(movieDuration), sessionEndTime);
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

    private SessionChangedEvent sessionChangedEvent(){
        return new SessionChangedEvent(
                SessionEventType.SESSION_EDITED, Instant.now(),
                id.value(), movieId, roomId, sessionBeginTime,
                sessionEndTime, movieDuration
        );
    }

    private SessionRemovedEvent sessionRemovedEvent(){
        return new SessionRemovedEvent(
                SessionEventType.SESSION_REMOVED, Instant.now(),
                id.value(), movieId, roomId, sessionBeginTime,
                sessionEndTime, movieDuration
        );
    }

    public void changeSessionTime(Duration duration){
        this.sessionBeginTime = sessionBeginTime.plus(duration);
        this.sessionEndTime = sessionEndTime.plus(duration);
        this.events.add(sessionChangedEvent());
    }

    public void removeSession(){
        if(this.sessionBeginTime.isBefore(LocalDateTime.now().plusDays(1)))
            throw new SessionException("não é possível remover sessões com menos de um dia para iniciar");

        this.events.add(sessionRemovedEvent());
    }

    // VALIDATIONS

    public void validateIfOverlapsWith(Session that){
        if (that == null) {
            return;
        }

        LocalDateTime start_A = this.sessionBeginTime;
        LocalDateTime end_A = this.sessionEndTime;
        LocalDateTime start_B = that.getSessionBeginTime();
        LocalDateTime end_B = that.getSessionEndTime();

        boolean overlaps = (start_A.isBefore(end_B) && start_B.isBefore(end_A))
                || start_B.equals(start_A) || end_A.equals(end_B);

        if (overlaps) {
            throw new SessionConflictException("Conflito de horário: a sessão se sobrepõe com uma sessão existente que ocorre de "
                    + start_B + " até " + end_B);
        }
    }

    private boolean hasSessionPeriodBegun() {
        return LocalDateTime.now().isAfter(this.sessionBeginTime);
    }

    private boolean hasSetupPeriodFinished() {
        return LocalDateTime.now().isAfter(this.sessionBeginTime.plusNanos(this.setupTime.toNanos()));
    }

    private boolean hasSessionPeriodFinished() {
        return LocalDateTime.now().isAfter(this.sessionEndTime);
    }

    private void validateData(){
        if(sessionBeginTime == null) throw new SessionException("data de inicio da sessão não pode estar nulo.");
        if(sessionEndTime == null) throw new SessionException("data de fim da sessão não pode estar nulo.");

        if(this.sessionBeginTime.isAfter(sessionEndTime))
            throw new SessionException("Data de início não pode estar após data de fim.");

        if(movieId == null) throw new SessionException("Id do filme não pode estar nulo.");
        if(roomId == null) throw new SessionException("Id da sala não pode estar nulo.");
        if(sessionScheduleState == null) sessionScheduleState = SessionScheduleState.SCHEDULED;


        if(sessionBeginTime.plus(setupTime).isAfter(sessionEndTime))
            throw new SessionException("A duração do setup não pode ser maior que o tempo total da sessão.");

        if(sessionBeginTime.plus(movieDuration).isAfter(sessionEndTime))
            throw new SessionException("A duração do filme não pode ser maior que o tempo total da sessão.");

    }

    public void validateNewSession(){
        if(this.sessionEndTime.isBefore(LocalDateTime.now()))
            throw new SessionException("Não é possível criar uma sessão anterior à data de hoje.");
    }

    // get / set

    public Duration getTotalSessionDuration(){
        return Duration.between(sessionBeginTime, sessionEndTime);
    }

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
