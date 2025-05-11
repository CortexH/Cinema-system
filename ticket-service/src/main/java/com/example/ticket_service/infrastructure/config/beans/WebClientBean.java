package com.example.ticket_service.infrastructure.config.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientBean {

    @Value("${application.url.room-service}")
    private String roomGatewayURL;

    @Bean
    public WebClient webClientRoomGateway(WebClient.Builder builder){
        return builder
                .baseUrl(roomGatewayURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}
