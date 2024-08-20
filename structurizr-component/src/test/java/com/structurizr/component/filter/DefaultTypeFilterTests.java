package com.structurizr.component.filter;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultTypeFilterTests {

    @Test
    void filter_ReturnsTrue() {
        assertTrue(new DefaultTypeFilter().accept(new Type("com.example.Class")));
    }

}