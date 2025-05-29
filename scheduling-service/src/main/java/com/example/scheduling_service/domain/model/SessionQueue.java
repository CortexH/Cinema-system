package com.example.scheduling_service.domain.model;

import com.example.scheduling_service.domain.enums.SessionScheduleState;
import com.example.scheduling_service.domain.valueObject.SessionQueueIdVO;

import java.time.LocalDateTime;

public class SessionQueue {

    private SessionQueueIdVO id;

    private String sessionMovieName;

    private String roomId;
    private String sessionId;

    private LocalDateTime sessionBeginTime;
    private LocalDateTime sessionEndTime;

    private SessionScheduleState sessionScheduleState;

    public SessionQueue(
            SessionQueueIdVO id, String sessionMovieName,
            String roomId, String sessionId,
            LocalDateTime sessionBeginTime, LocalDateTime sessionEndTime,
            SessionScheduleState sessionScheduleState
    ) {
        this.id = id;
        this.sessionMovieName = sessionMovieName;
        this.roomId = roomId;
        this.sessionId = sessionId;
        this.sessionBeginTime = sessionBeginTime;
        this.sessionEndTime = sessionEndTime;
        this.sessionScheduleState = sessionScheduleState;
    }



}
