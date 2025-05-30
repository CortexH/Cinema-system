package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;

import java.time.Duration;
import java.time.LocalDateTime;

public class Session {

    private SessionIdVO id;

    private String movieName;

    private String roomId;

    private LocalDateTime sessionBeginTime;
    private LocalDateTime sessionEndTime;

    private Duration setupTime;
    private Duration finishTime;

    private SessionScheduleState sessionScheduleState;

    public Session(
            SessionIdVO id, String movieName,
            String roomId, LocalDateTime sessionBeginTime,
            LocalDateTime sessionEndTime,
            SessionScheduleState sessionScheduleState,
            Duration setupTime, Duration finishTime

    ) {
        this.id = id;
        this.movieName = movieName;
        this.roomId = roomId;
        this.sessionBeginTime = sessionBeginTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionScheduleState = sessionScheduleState;
        this.setupTime = setupTime;
        this.finishTime = finishTime;
    }



}
