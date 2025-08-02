package com.example.scheduling_service.domain.port.sessionEdit;

import com.example.scheduling_service.domain.model.SessionEditCommand;

import java.util.List;
import java.util.Optional;

public interface SessionEditCommandPort {

    Optional<SessionEditCommand> saveSingle(SessionEditCommand editCommand);
    void saveInBatch(List<SessionEditCommand> editCommands);
    void markAllAsProcessed(List<SessionEditCommand> items);

}
