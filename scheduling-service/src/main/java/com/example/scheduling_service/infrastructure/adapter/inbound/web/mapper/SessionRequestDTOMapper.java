package com.example.scheduling_service.infrastructure.adapter.inbound.web.mapper;

import com.example.scheduling_service.application.dto.request.SessionRequestDTO;
import com.example.scheduling_service.domain.model.Session;
import com.example.scheduling_service.domain.valueObject.SessionIdVO;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class SessionRequestDTOMapper {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static Session toInbound(SessionRequestDTO data){

        Duration setupDuration = Duration.ofSeconds(data.setup_duration());
        LocalDateTime beginTime = LocalDateTime.parse(data.session_begin_time(), dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(data.session_end_time(), dateTimeFormatter);

        Duration movieDuration = Duration.ofSeconds(
                (data.movie_duration() == null) ? 0 : data.movie_duration()
        );

        return new Session(
                SessionIdVO.generate(), data.movie_id(),
                data.room_id(), beginTime, endTime, setupDuration,
                movieDuration
        );
    }
}
