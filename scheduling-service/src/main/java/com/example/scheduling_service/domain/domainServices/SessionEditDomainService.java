package com.example.scheduling_service.domain.domainServices;

import com.example.scheduling_service.domain.dtos.EditedSessionDTO;
import com.example.scheduling_service.domain.dtos.SessionEditDTO;
import com.example.scheduling_service.domain.enums.CommandStatus;
import com.example.scheduling_service.domain.exception.SessionConflictException;
import com.example.scheduling_service.domain.exception.SessionException;
import com.example.scheduling_service.domain.exception.SessionStateException;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditCommandPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class SessionEditDomainService {

    private final SessionQueryRepositoryPort sessionQueryPort;
    private final SessionEditQueryPort sessionEditQueryPort;

    public SessionEditDomainService(
            SessionQueryRepositoryPort sessionQueryPort,
            SessionEditQueryPort sessionEditQueryPort
    ){
        this.sessionQueryPort = sessionQueryPort;
        this.sessionEditQueryPort = sessionEditQueryPort;
    }

    public List<SessionEditCommand> rearrangeNextSessions(Session session){

        List<Session> nextSessions = sessionQueryPort.findAllNextSessionsFrom(session);

        Duration minusDuration = session.getTotalSessionDuration()
                .negated();

        List<SessionEditCommand> allEditCommands = new ArrayList<>();

        for(Session that : nextSessions){
            that.changeOverallTime(minusDuration);

            SessionEditCommand command = new SessionEditCommand(
                    UUID.randomUUID(),
                    that.getId().value(), that.getMovieId(),
                    that.getMovieId(), that.getSessionBeginTime(),
                    that.getSessionEndTime(), that.getSetupTime(),
                    that.getMovieDuration(), that.getSessionScheduleState(),
                    null, null,
                    CommandStatus.PENDING
            );

            allEditCommands.add(command);
        }

        return allEditCommands;
    }

    public EditedSessionDTO editSession(Session usedSession, List<SessionEditCommand> editCommands){

        List<SessionEditCommand> newCommands = new ArrayList<>();

        for(SessionEditCommand command : editCommands) {
            try{
                if(usedSession == null) throw new SessionException("Não é possível realizar edição pois a sessão é nula.");

                usedSession.editSession(new SessionEditDTO(
                        command.movieId(), command.roomId(),
                        command.sessionBeginTime(), command.sessionEndTime(),
                        command.setupTime(), command.movieDuration(),
                        command.sessionScheduleState()));

                newCommands.add(new SessionEditCommand(
                        command.id(), command.relatedSessionId(),
                        command.movieId(), command.roomId(),
                        command.sessionBeginTime(), command.sessionEndTime(),
                        command.setupTime(), command.movieDuration(),
                        command.sessionScheduleState(), LocalDateTime.now(),
                        command.createdAt(), CommandStatus.PROCESSED
                ));

            } catch (SessionException | SessionConflictException | SessionStateException e) {
                newCommands.add(new SessionEditCommand(
                        command.id(), command.relatedSessionId(),
                        command.movieId(), command.roomId(),
                        command.sessionBeginTime(), command.sessionEndTime(),
                        command.setupTime(), command.movieDuration(),
                        command.sessionScheduleState(), LocalDateTime.now(),
                        command.createdAt(), CommandStatus.FAILED
                ));
            }

        }

        return new EditedSessionDTO(usedSession, newCommands);
    }

}
