package com.example.scheduling_service.infrastructure.adapter.outbound.web.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SpecificSchedule {

    private LocalDateTime timeBegin;

}
