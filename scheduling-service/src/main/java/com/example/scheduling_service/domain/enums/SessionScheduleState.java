package com.example.scheduling_service.domain.enums;

public enum SessionScheduleState {

    SCHEDULED(1), // sessão não começou ainda
    SETUP_IN_PROGRESS(2),// setup em progresso
    NOW_WORKING(3), // sessão está ativa, com um filme rodando
    FINISHED(4); // sessão terminou

    private final int order;

    SessionScheduleState(int i) {
        this.order = i;
    }

    public int getOrder() {
        return order;
    }

    public boolean canTransitTo(SessionScheduleState state){
        return (state.order == this.order + 1);
    }

}
