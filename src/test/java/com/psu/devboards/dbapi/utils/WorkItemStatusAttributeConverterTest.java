package com.psu.devboards.dbapi.utils;

import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class WorkItemStatusAttributeConverterTest {

    WorkItemStatusAttributeConverter workItemStatusAttributeConverter;

    @BeforeEach
    void setUp() {
        workItemStatusAttributeConverter = new WorkItemStatusAttributeConverter();
    }

    @Test
    void convertToDatabaseColumnReturnsNullIfStatusIsNull() {
        String column = workItemStatusAttributeConverter.convertToDatabaseColumn(null);
        assertNull(column);
    }

    @Test
    void convertToEntityAttributeReturnsNullIfStatusIsNull() {
        WorkItemStatus attribute = workItemStatusAttributeConverter.convertToEntityAttribute(null);
        assertNull(attribute);
    }
}