package com.example.scheduling_service.infrastructure.adapter.inbound.scheduler;

import com.example.scheduling_service.domain.port.in.SessionSchedulerUseCase;
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

    @Scheduled(cron = "0 */5 * * * *")
    public void run(){
        log.info("TESTE");
        sessionSchedulerUseCase.runScheduledCheckout();
    }

}
