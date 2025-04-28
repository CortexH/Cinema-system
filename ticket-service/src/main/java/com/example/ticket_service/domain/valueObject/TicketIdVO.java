package com.example.ticket_service.domain.valueObject;

import java.util.Objects;
import java.util.UUID;

public record TicketIdVO(UUID value) {

    public TicketIdVO {
        Objects.requireNonNull(value, "Id do ticket não pode ser nulo");
    }

    public static TicketIdVO generate() {
        return new TicketIdVO(UUID.randomUUID());
    }

    public static TicketIdVO from(String id){
        Objects.requireNonNull(id, "Id não pode ser nulo");
        try{
            return new TicketIdVO(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("formato inválido de string para UUID", e);
        }
    }

    public static TicketIdVO from(UUID id){
        return new TicketIdVO(id);
    }

}
