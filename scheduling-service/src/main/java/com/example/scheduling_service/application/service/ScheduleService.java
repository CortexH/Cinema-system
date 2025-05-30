package com.example.scheduling_service.application.service;

import com.example.scheduling_service.domain.port.in.SessionSchedulerUseCase;
import org.springframework.stereotype.Component;

@Component
public class ScheduleService implements SessionSchedulerUseCase {

    @Override
    public void scheduleNextSessions(Integer days) {

    }

    @Override
    public void rescheduleActualSessions() {

    }

    @Override
    public void runScheduledCheckout() {

    }
}
