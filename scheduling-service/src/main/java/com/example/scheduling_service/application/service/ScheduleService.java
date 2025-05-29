package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.port.in.SchedulingUseCase;
import com.example.scheduling_service.domain.valueObject.SessionQueueIdVO;

import java.util.List;

public class ScheduleService implements SchedulingUseCase {

    @Override
    public void scheduleNextSessions(Integer days) {



    }

    @Override
    public void rescheduleActualSessions() {

    }

    @Override
    public void runScheduledCheckout() {

    }

    @Override
    public void removeAndReplaceScheduledSession(Boolean replace, List<SessionQueueIdVO> sessionId) {

    }
}
