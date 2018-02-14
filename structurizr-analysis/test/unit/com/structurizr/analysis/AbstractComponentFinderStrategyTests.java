package com.structurizr.analysis;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AbstractComponentFinderStrategyTests {

    private AbstractComponentFinderStrategy strategy = new TypeMatcherComponentFinderStrategy();

    @Test
    public void test_addSupportingTypesStrategy_ThrowsAnException_WhenANullSupportingTypesStrategyIsSpecified() {
        try {
            strategy.addSupportingTypesStrategy(null);
            fail();
        } catch (IllegalArgumentException iae) {
            assertEquals("A supporting types strategy must be provided.", iae.getMessage());
        }
    }

}
