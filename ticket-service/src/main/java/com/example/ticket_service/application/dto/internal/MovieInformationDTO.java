package com.example.ticket_service.application.dto.internal;

import java.time.LocalDateTime;

public record MovieInformationDTO(
        String name,
        LocalDateTime time,
        Boolean subtitles,
        Integer ageGroup, // faixa etária
        String ageGroupReason, // motivo da faixa etária
        Boolean accessibility
) {

}
