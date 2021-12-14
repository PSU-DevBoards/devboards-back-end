package com.psu.devboards.dbapi.utils;

import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

/**
 * Converts the {@link WorkItemStatus} enum to and from a more friendly string format for database storage.
 */
@Component
@Converter(autoApply = true)
public class WorkItemStatusAttributeConverter implements AttributeConverter<WorkItemStatus, String> {

    @Override
    public String convertToDatabaseColumn(WorkItemStatus status) {
        if (status == null) return null;

        return status.getType();
    }

    @Override
    public WorkItemStatus convertToEntityAttribute(String status) {
        if (status == null) return null;

        return Stream.of(WorkItemStatus.values())
                .filter(t -> t.getType().equals(status))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

