package com.example.scheduling_service.infrastructure.adapter.inbound.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MovieSchedulingService {



    @Scheduled(cron = "* * * * * *")
    public void run(){

    }

}
