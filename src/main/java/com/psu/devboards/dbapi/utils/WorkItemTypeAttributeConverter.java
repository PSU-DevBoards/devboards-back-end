package com.psu.devboards.dbapi.utils;

import com.psu.devboards.dbapi.models.entities.WorkItemType;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Component
@Converter(autoApply = true)
public class WorkItemTypeAttributeConverter implements AttributeConverter<WorkItemType, String> {

    @Override
    public String convertToDatabaseColumn(WorkItemType type) {
        if (type == null) return null;

        return type.getType();
    }

    @Override
    public WorkItemType convertToEntityAttribute(String type) {
        if (type == null) return null;

        return Stream.of(WorkItemType.values())
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
