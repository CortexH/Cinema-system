package com.example.scheduling_service.domain.enums;

public enum CommandStatusReason {

    NO_REASON(1, "Command falhou por motivo desconhecido"),
    SESSION_DELETED(2, "Sessão removida ou inexistente para prosseguir"),
    SUCCESSFUL(10, "Sucesso ao prosseguir"),
    PENDING(11, "Aguardando processo");

    private final int code;
    private final String reason;

    CommandStatusReason(int i, String s) {
        this.code = i;
        this.reason = s;
    }

    public int getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
