package com.structurizr.component.filter;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExcludeAbstractClassesFilterTests {

    @Test
    void filter_ReturnsTrue_WhenTheTypeIsNotAbstract() {
        assertTrue(new ExcludeAbstractClassesTypeFilter().accept(new Type("com.example.Class") {
            @Override
            public boolean isAbstractClass() {
                return false;
            }
        }));
    }

    @Test
    void filter_ReturnsFalse_WhenTheTypeIsAbstract() {
        assertFalse(new ExcludeAbstractClassesTypeFilter().accept(new Type("com.example.Class") {
            @Override
            public boolean isAbstractClass() {
                return true;
            }
        }));
    }

}