package com.example.scheduling_service.infrastructure.adapter.inbound.scheduler;

import com.example.scheduling_service.application.port.in.SessionSchedulerUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SessionSchedulingService {

    private final SessionSchedulerUseCase sessionSchedulerUseCase;

    @Scheduled(cron = "0 */1 * * * *")
    public void run(){
        sessionSchedulerUseCase.runScheduledCheckout();
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void runDailyMovieSchedule(){
        sessionSchedulerUseCase.scheduleNextSessions(1);
    }

}
