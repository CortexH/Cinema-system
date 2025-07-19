package com.example.scheduling_service.infrastructure.config.bean;

import com.example.scheduling_service.domain.domainServices.SessionDomainService;
import com.example.scheduling_service.domain.port.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.SessionQueryRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServicesBeans {

    @Bean
    public SessionDomainService sessionDomainService(
            SessionQueryRepositoryPort queryPort,
            SessionCommandRepositoryPort commandPort,
            SessionEventPublisherPort publisherPort
    ){
        return new SessionDomainService(queryPort, commandPort, publisherPort);
    }

}
