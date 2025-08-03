package com.example.scheduling_service.domain.port.sessionEdit;

import com.example.scheduling_service.domain.model.SessionEditCommand;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionEditQueryPort {

    List<SessionEditCommand> findBySessionId(SessionIdVO id);
    List<SessionEditCommand> findAllPendingOrdered();

    List<SessionEditCommand> findAllPendingByTimeRangeOrdered(LocalDateTime beginTime, LocalDateTime endTime);

}
