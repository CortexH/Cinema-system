package com.example.scheduling_service.infrastructure.adapter.inbound.scheduler;

import com.example.scheduling_service.domain.port.in.SchedulingUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MovieSchedulingService {

    private final SchedulingUseCase schedulingUseCase;

    @Scheduled(cron = "* * * * * *")
    public void run(){
        schedulingUseCase.runScheduledCheckout();
    }

}
