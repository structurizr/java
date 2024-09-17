package com.structurizr.component;

import com.structurizr.component.description.FirstSentenceDescriptionStrategy;
import com.structurizr.component.description.TruncatedDescriptionStrategy;
import com.structurizr.component.filter.ExcludeTypesByRegexFilter;
import com.structurizr.component.filter.IncludeTypesByRegexFilter;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import com.structurizr.component.naming.FullyQualifiedNamingStrategy;
import com.structurizr.component.naming.TypeNamingStrategy;
import com.structurizr.component.supporting.AllTypesInPackageSupportingTypesStrategy;
import com.structurizr.component.supporting.AllTypesUnderPackageSupportingTypesStrategy;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComponentFinderStrategyBuilderTests {

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
    void withName_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().withName(null);
            fail();
        } catch (Exception e) {
            assertEquals("A naming strategy must be provided", e.getMessage());
        }
    }

    @Test
    void withName_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().withName(new TypeNamingStrategy()).withName(new FullyQualifiedNamingStrategy());
            fail();
        } catch (Exception e) {
            assertEquals("A naming strategy has already been configured", e.getMessage());
        }
    }

    @Test
    void withDescription_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().withDescription(null);
            fail();
        } catch (Exception e) {
            assertEquals("A description strategy must be provided", e.getMessage());
        }
    }

    @Test
    void withDescription_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().withDescription(new TruncatedDescriptionStrategy(50)).withDescription(new FirstSentenceDescriptionStrategy());
            fail();
        } catch (Exception e) {
            assertEquals("A description strategy has already been configured", e.getMessage());
        }
    }

    @Test
    void forTechnology_ThrowsAnException_WhenPassedNull() {
        try {
            new ComponentFinderStrategyBuilder().forTechnology(null);
            fail();
        } catch (Exception e) {
            assertEquals("A technology must be provided", e.getMessage());
        }
    }

    @Test
    void forTechnology_ThrowsAnException_WhenPassedAnEmptyString() {
        try {
            new ComponentFinderStrategyBuilder().forTechnology("");
            fail();
        } catch (Exception e) {
            assertEquals("A technology must be provided", e.getMessage());
        }
    }

    @Test
    void forTechnology_ThrowsAnException_WhenCalledTwice() {
        try {
            new ComponentFinderStrategyBuilder().forTechnology("X").forTechnology("Y");
            fail();
        } catch (Exception e) {
            assertEquals("A technology has already been configured", e.getMessage());
        }
    }

    @Test
    void build_ThrowsAnException_WhenATypeMatcherHasNotBeenConfigured() {
        try {
            new ComponentFinderStrategyBuilder().build();
            fail();
        } catch (Exception e) {
            assertEquals("A type matcher must be provided", e.getMessage());
        }
    }

    @Test
    void build() {
        ComponentFinderStrategy strategy = new ComponentFinderStrategyBuilder()
                .forTechnology("Spring MVC Controller")
                .matchedBy(new NameSuffixTypeMatcher("Controller"))
                .filteredBy(new IncludeTypesByRegexFilter("com.example.web.\\.*"))
                .withName(new TypeNamingStrategy())
                .withDescription(new FirstSentenceDescriptionStrategy())
                .build();

        assertEquals("ComponentFinderStrategy{technology='Spring MVC Controller', typeMatcher=NameSuffixTypeMatcher{suffix='Controller'}, typeFilter=IncludeTypesByRegexFilter{regex='com.example.web.\\.*'}, supportingTypesStrategy=DefaultSupportingTypesStrategy{}, namingStrategy=TypeNamingStrategy{}, descriptionStrategy=FirstSentenceDescriptionStrategy{}, componentVisitor=DefaultComponentVisitor{}}", strategy.toString());
    }

}