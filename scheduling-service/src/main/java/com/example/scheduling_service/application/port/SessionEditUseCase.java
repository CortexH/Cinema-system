package com.example.scheduling_service.application.port;

import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

public interface SessionEditUseCase {

    Session runSessionPendingCommands(SessionIdVO sessionIdVO);

}
