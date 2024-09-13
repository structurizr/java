package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
import com.structurizr.component.matcher.AnnotationTypeMatcher;
import com.structurizr.component.matcher.NameSuffixTypeMatcher;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ComponentFinderStrategyParserTests extends AbstractTests {

    private final ComponentFinderStrategyParser parser = new ComponentFinderStrategyParser();
    private final ComponentFinderBuilder componentFinderBuilder = new ComponentFinderBuilder();
    private final ComponentFinderStrategyDslContext context = new ComponentFinderStrategyDslContext(componentFinderBuilder);

    @Test
    void test_parseTechnology_ThrowsAnException_WhenThereAreTooFewTokens() {
        try {
            parser.parseTechnology(context, tokens("technology"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: technology <name>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseTechnology(context, tokens("technology", "name", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: technology <name>", e.getMessage());
        }
    }

    @Test
    void test_parseTechnology() {
        parser.parseTechnology(context, tokens("technology", "name"));
        assertEquals("ComponentFinderStrategyBuilder{technology='name', typeMatcher=null, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseMatcher(context, tokens("matcher"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: matcher <annotation|extends|implements|name-suffix|fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenTheAnnotationTypeMatcherIsUsedAndThereAreTooFewTokens() {
        try {
            parser.parseMatcher(context, tokens("matcher", "annotation"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: matcher annotation <fqn>", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenTheAnnotationTypeMatcherIsUsed() {
        parser.parseMatcher(context, tokens("matcher", "annotation", "com.example.Component"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=AnnotationTypeMatcher{annotationType='Lcom/example/Component;'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenTheExtendsTypeMatcherIsUsedAndThereAreTooFewTokens() {
        try {
            parser.parseMatcher(context, tokens("matcher", "extends"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: matcher extends <fqn>", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenTheExtendsTypeMatcherIsUsed() {
        parser.parseMatcher(context, tokens("matcher", "extends", "com.example.Component"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=ExtendsTypeMatcher{className='com.example.Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenTheImplementsTypeMatcherIsUsedAndThereAreTooFewTokens() {
        try {
            parser.parseMatcher(context, tokens("matcher", "implements"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: matcher implements <fqn>", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenTheImplementsTypeMatcherIsUsed() {
        parser.parseMatcher(context, tokens("matcher", "implements", "com.example.Component"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=ImplementsTypeMatcher{interfaceName='com.example.Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenTheNameSuffixTypeMatcherIsUsedAndThereAreTooFewTokens() {
        try {
            parser.parseMatcher(context, tokens("matcher", "name-suffix"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: matcher name-suffix <name>", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenTheNameSuffixTypeMatcherIsUsed() {
        parser.parseMatcher(context, tokens("matcher", "name-suffix", "Component"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=NameSuffixTypeMatcher{suffix='Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenTheFullyQualifiedNameRegexTypeMatcherIsUsedAndThereAreTooFewTokens() {
        try {
            parser.parseMatcher(context, tokens("matcher", "fqn-regex"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Expected: matcher fqn-regex <regex>", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenTheRegexTypeMatcherIsUsed() {
        parser.parseMatcher(context, tokens("matcher", "fqn-regex", ".*Component"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=RegexTypeMatcher{regex='.*Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenACustomTypeMatcherIsUsedButCannotBeLoaded() {
        try {
            parser.parseMatcher(context, tokens("matcher", "com.example.CustomTypeMatcher"), new File("."));
            fail();
        } catch (Exception e) {
            assertEquals("Type matcher \"com.example.CustomTypeMatcher\" could not be loaded - class java.lang.ClassNotFoundException: com.example.CustomTypeMatcher", e.getMessage());
        }
    }

    @Test
    void test_parseMatcher_WhenACustomTypeMatcherIsUsedWithoutParameters() {
        parser.parseMatcher(context, tokens("matcher", "com.structurizr.dsl.example.CustomTypeMatcher"), new File("."));
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=CustomTypeMatcher{}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenACustomTypeMatcherIsUsedWithAParameter() {
        parser.parseMatcher(context, tokens("matcher", NameSuffixTypeMatcher.class.getCanonicalName(), "Component"), new File("."));
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=NameSuffixTypeMatcher{suffix='Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseFilter_ThrowsAnException_WhenNoModeAndTypeAreSpecified() {
        try {
            parser.parseFilter(context, tokens("filter"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: filter <include|exclude> <fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseFilter_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseFilter(context, tokens("filter", "include"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: filter <include|exclude> <fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseFilter_ThrowsAnException_WhenInvalidModeIsSpecified() {
        try {
            parser.parseFilter(context, tokens("filter", "mode", "fqn-regex"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Filter mode should be \"include\" or \"exclude\": filter <include|exclude> <fqn-regex> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseFilter_WhenIncludeFullyQualifiedNameRegexTypeFilterIsUsed() {
        parser.parseFilter(context, tokens("filter", "include", "fqn-regex", ".*"), new File("."));
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=IncludeTypesByRegexFilter{regex='.*'}, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseFilter_WhenExcludeFullyQualifiedNameRegexTypeFilterIsUsed() {
        parser.parseFilter(context, tokens("filter", "exclude", "fqn-regex", ".*"), new File("."));
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=ExcludeTypesByRegexFilter{regex='.*'}, supportingTypesStrategy=null, namingStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseSupportingTypes_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseSupportingTypes(context, tokens("supportingTypes"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: supportingTypes <all-referenced|referenced-in-package|in-package|under-package|none> [parameters]", e.getMessage());
        }
    }

    @Test
    void test_parseNaming_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseNaming(context, tokens("naming"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: naming <name|fqn|package>", e.getMessage());
        }
    }

}