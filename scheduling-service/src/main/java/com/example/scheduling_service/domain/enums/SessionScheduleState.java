package com.example.scheduling_service.domain.enums;

public enum SessionScheduleState {

    SCHEDULED, // sessão não começou ainda
    NOW_WORKING, // sessão está ativa, com um filme rodando
    FINISHED, // sessão terminou
    SETUP_IN_PROGRESS // setup em progresso

}
