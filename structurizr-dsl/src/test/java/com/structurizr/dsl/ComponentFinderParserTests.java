package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ComponentFinderParserTests extends AbstractTests {

    private final ComponentFinderParser parser = new ComponentFinderParser();
    private final ComponentFinderDslContext context = new ComponentFinderDslContext(null, null);

    @Test
    void test_parseFilter_ThrowsAnException_WhenNoModeAndTypeAreSpecified() {
        try {
            parser.parseFilter(context, tokens("filter"));
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: filter <include|exclude> <fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseFilter_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseFilter(context, tokens("filter", "include"));
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: filter <include|exclude> <fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseFilter_ThrowsAnException_WhenInvalidModeIsSpecified() {
        try {
            parser.parseFilter(context, tokens("filter", "mode", "fqn-regex"));
            fail();
        } catch (Exception e) {
            assertEquals("Filter mode should be \"include\" or \"exclude\": filter <include|exclude> <fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseFilter_WhenIncludeFullyQualifiedNameRegexTypeFilterIsUsed() {
        parser.parseFilter(context, tokens("filter", "include", "fqn-regex", ".*"));
        assertEquals("ComponentFinderBuilder{container=null, typeProviders=[], typeFilter=IncludeFullyQualifiedNameRegexFilter{regex='.*'}, componentFinderStrategies=[]}", context.getComponentFinderBuilder().toString());
    }

    @Test
    void test_parseFilter_WhenExcludeFullyQualifiedNameRegexTypeFilterIsUsed() {
        parser.parseFilter(context, tokens("filter", "exclude", "fqn-regex", ".*"));
        assertEquals("ComponentFinderBuilder{container=null, typeProviders=[], typeFilter=ExcludeFullyQualifiedNameRegexFilter{regex='.*'}, componentFinderStrategies=[]}", context.getComponentFinderBuilder().toString());
    }

}