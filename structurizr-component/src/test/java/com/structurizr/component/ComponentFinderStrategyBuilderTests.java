package com.structurizr.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

public class ComponentFinderStrategyBuilderTests {

    @Test
    void build_ThrowsAnException_WhenATypeMatcherHasNotBeenConfigured() {
        assertThrowsExactly(RuntimeException.class, () -> new ComponentFinderStrategyBuilder().build());
    }

}