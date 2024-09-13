package com.structurizr.component;

import com.structurizr.component.filter.ExcludeTypesByRegexFilter;
import com.structurizr.component.filter.IncludeTypesByRegexFilter;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import com.structurizr.component.naming.FullyQualifiedNamingStrategy;
import com.structurizr.component.naming.SimpleNamingStrategy;
import com.structurizr.component.supporting.AllTypesInPackageSupportingTypesStrategy;
import com.structurizr.component.supporting.AllTypesUnderPackageSupportingTypesStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentFinderStrategyBuilderTests {

    @Test
    void build_ThrowsAnException_WhenATypeMatcherHasNotBeenConfigured() {
        assertThrowsExactly(RuntimeException.class, () -> new ComponentFinderStrategyBuilder().build());
    }

    @Test
    void matchedBy_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().matchedBy(null);
            fail();
        } catch (Exception e) {
            assertEquals("A type matcher must be provided", e.getMessage());
        }
    }

    @Test
    void matchedBy_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().matchedBy(new NameSuffixTypeMatcher("X")).matchedBy(new NameSuffixTypeMatcher("Y"));
            fail();
        } catch (Exception e) {
            assertEquals("A type matcher has already been configured", e.getMessage());
        }
    }

    @Test
    void filteredBy_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().filteredBy(null);
            fail();
        } catch (Exception e) {
            assertEquals("A type filter must be provided", e.getMessage());
        }
    }

    @Test
    void filteredBy_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().filteredBy(new IncludeTypesByRegexFilter(".*")).filteredBy(new ExcludeTypesByRegexFilter(".*"));
            fail();
        } catch (Exception e) {
            assertEquals("A type filter has already been configured", e.getMessage());
        }
    }

    @Test
    void supportedBy_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().supportedBy(null);
            fail();
        } catch (Exception e) {
            assertEquals("A supporting types strategy must be provided", e.getMessage());
        }
    }

    @Test
    void supportedBy_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().supportedBy(new AllTypesInPackageSupportingTypesStrategy()).supportedBy(new AllTypesUnderPackageSupportingTypesStrategy());
            fail();
        } catch (Exception e) {
            assertEquals("A supporting types strategy has already been configured", e.getMessage());
        }
    }

    @Test
    void namedBy_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().namedBy(null);
            fail();
        } catch (Exception e) {
            assertEquals("A naming strategy must be provided", e.getMessage());
        }
    }

    @Test
    void namedBy_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().namedBy(new SimpleNamingStrategy()).namedBy(new FullyQualifiedNamingStrategy());
            fail();
        } catch (Exception e) {
            assertEquals("A naming strategy has already been configured", e.getMessage());
        }
    }

    @Test
    void asTechnology_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().asTechnology(null);
            fail();
        } catch (Exception e) {
            assertEquals("A technology must be provided", e.getMessage());
        }
    }

    @Test
    void asTechnology_ThrowsAnException_WhenPassedAnEmptyString() {
        try {
            new ComponentFinderStrategyBuilder().asTechnology("");
            fail();
        } catch (Exception e) {
            assertEquals("A technology must be provided", e.getMessage());
        }
    }

    @Test
    void asTechnology_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().asTechnology("X").asTechnology("Y");
            fail();
        } catch (Exception e) {
            assertEquals("A technology has already been configured", e.getMessage());
        }
    }

}