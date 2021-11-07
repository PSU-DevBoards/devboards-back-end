package com.psu.devboards.dbapi.utils;

import com.psu.devboards.dbapi.models.entities.WorkItemType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class WorkItemTypeAttributeConverterTest {

    WorkItemTypeAttributeConverter workItemTypeAttributeConverter;

    @BeforeEach
    void setUp() {
        workItemTypeAttributeConverter = new WorkItemTypeAttributeConverter();
    }

    @Test
    void convertToDatabaseColumnReturnsNullIfTypeIsNull() {
        String column = workItemTypeAttributeConverter.convertToDatabaseColumn(null);
        assertNull(column);
    }

    @Test
    void convertToEntityAttributeReturnsNullIfTypeIsNull() {
        WorkItemType attribute = workItemTypeAttributeConverter.convertToEntityAttribute(null);
        assertNull(attribute);
    }
}