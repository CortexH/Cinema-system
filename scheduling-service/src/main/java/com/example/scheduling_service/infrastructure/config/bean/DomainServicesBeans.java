package com.example.scheduling_service.infrastructure.config.bean;

import com.example.scheduling_service.domain.domainServices.SessionDomainService;
import com.example.scheduling_service.domain.domainServices.SessionEditDomainService;
import com.example.scheduling_service.domain.port.session.SessionCommandRepositoryPort;
import com.example.scheduling_service.domain.port.session.SessionEventPublisherPort;
import com.example.scheduling_service.domain.port.session.SessionQueryRepositoryPort;
import com.example.scheduling_service.domain.port.sessionEdit.SessionEditQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainServicesBeans {

    @Bean
    public SessionDomainService sessionDomainService(
            SessionQueryRepositoryPort queryPort
    ){
        return new SessionDomainService(queryPort);
    }

    @Bean
    public SessionEditDomainService sessionEditDomainService(
            SessionQueryRepositoryPort sessionQueryRepositoryPort,
            SessionEditQueryPort sessionEditQueryPort

    ){
        return new SessionEditDomainService(sessionQueryRepositoryPort, sessionEditQueryPort);
    }


}
