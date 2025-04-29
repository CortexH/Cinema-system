package com.example.ticket_service.domain.valueObject;

import java.time.LocalDateTime;

public record ExpireDateVO(LocalDateTime time) {

    public static ExpireDateVO from(LocalDateTime value){
        return new ExpireDateVO(value);
    }

    public static ExpireDateVO generate(LocalDateTime movieTime){
        return new ExpireDateVO(movieTime.plusHours(2L));
    }

}
