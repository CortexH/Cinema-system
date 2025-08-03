package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.implementation;

import com.example.scheduling_service.domain.enums.CommandStatus;
import com.example.scheduling_service.domain.enums.CommandStatusReason;
import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditCommandPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.mapper.SessionEditCommandMapper;
import com.example.scheduling_service.infrastructure.adapter.outbound.persistence.repository.repository.SessionEditRepositoryJPA;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SessionEditRepositoryAdapter implements SessionEditCommandPort, SessionEditQueryPort {

    private final SessionEditRepositoryJPA repository;

    @Override
    public List<SessionEditCommand> findBySessionId(SessionIdVO id) {
        return new ArrayList<>(repository.findBySessionIdAndOrder(id.value())
                .stream().map(SessionEditCommandMapper::toInbound).toList());
    }

    @Override
    public List<SessionEditCommand> findAllPendingOrdered() {
        return new ArrayList<>(repository.findAllPendingAndOrder()
                .stream().map(SessionEditCommandMapper::toInbound).toList());
    }

    @Override
    public List<SessionEditCommand> findAllPendingByTimeRangeOrdered(LocalDateTime beginTime, LocalDateTime endTime) {
        return new ArrayList<>(repository.findAllPendingByTimeRangeOrdered(beginTime, endTime)
                .stream().map(SessionEditCommandMapper::toInbound).toList());
    }

    @Override
    public void markAllAsProcessed(List<SessionEditCommand> items) {
        repository.saveAll(items.stream().map(i ->
            SessionEditCommandMapper.toOutbound(new SessionEditCommand(
                    i.id(), i.relatedSessionId(), i.movieId(), i.roomId(),
                    i.sessionBeginTime(), i.sessionEndTime(), i.setupTime(),
                    i.movieDuration(), i.sessionScheduleState(), LocalDateTime.now(),
                    i.createdAt(), CommandStatus.PROCESSED
            ))
        ).toList());
    }

    @Override
    public void markAsFailed(SessionEditCommand command, CommandStatusReason reason) {

    }

    @Override
    public void markAllAsFailed(List<SessionEditCommand> commands, CommandStatusReason reason) {
        if(reason == null) reason = CommandStatusReason.NO_REASON;

        repository.saveAll(commands.stream().map(i ->
                SessionEditCommandMapper.toOutbound(new SessionEditCommand(
                        i.id(), i.relatedSessionId(), i.movieId(), i.roomId(),
                        i.sessionBeginTime(), i.sessionEndTime(), i.setupTime(),
                        i.movieDuration(), i.sessionScheduleState(), LocalDateTime.now(),
                        i.createdAt(), CommandStatus.FAILED
                ))
        ).toList());
    }


    @Override
    public Optional<SessionEditCommand> saveSingle(SessionEditCommand editCommand) {
        return Optional.empty();
    }

    @Override
    public void saveInBatch(List<SessionEditCommand> editCommands) {
        repository.saveAll(editCommands.stream().map(SessionEditCommandMapper::toOutbound).toList());
    }
}
