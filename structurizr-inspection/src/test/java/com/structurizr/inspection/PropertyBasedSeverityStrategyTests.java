package com.structurizr.inspection;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertyBasedSeverityStrategyTests {

    @Test
    void generateTypes() {
        PropertyBasedSeverityStrategy strategy = new PropertyBasedSeverityStrategy();
        List<String> types = strategy.generatePropertyNames("model.component.description");

        assertEquals("structurizr.inspection.model.component.description", types.get(0));
        assertEquals("structurizr.inspection.model.component.*", types.get(1));
        assertEquals("structurizr.inspection.model.*", types.get(2));
        assertEquals("structurizr.inspection.*", types.get(3));
    }

}