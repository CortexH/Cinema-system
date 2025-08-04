package com.example.scheduling_service.application.port;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.util.Optional;

public interface SessionEditUseCase {

    void changePendingCommandsStatusToFailed(SessionIdVO sessionIdVO);
    Session runSessionPendingCommands(SessionIdVO sessionIdVO);

    Optional<SessionEditCommand> findLastPendingEditCommand(SessionIdVO sessionIdVO);

}
