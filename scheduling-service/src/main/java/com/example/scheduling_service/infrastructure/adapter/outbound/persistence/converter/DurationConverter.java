package com.example.scheduling_service.infrastructure.adapter.outbound.persistence.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {


    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return duration != null ? duration.toNanos() : null;
    }

    @Override
    public Duration convertToEntityAttribute(Long aLong) {
        return aLong != null ? Duration.ofNanos(aLong) : null;
    }
}
