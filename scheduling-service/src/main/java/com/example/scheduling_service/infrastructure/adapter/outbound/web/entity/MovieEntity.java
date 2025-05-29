package com.example.scheduling_service.infrastructure.adapter.outbound.web.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {

    private UUID movieID;
    private String movieName;

    private Duration duration;
    private LocalDateTime validUntil;
    private Integer timesPerDay;

}
