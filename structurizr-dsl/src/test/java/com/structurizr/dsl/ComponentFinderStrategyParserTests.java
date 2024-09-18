package com.structurizr.dsl;

import com.structurizr.component.ComponentFinderBuilder;
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
        assertEquals("ComponentFinderStrategyBuilder{technology='name', typeMatcher=null, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=AnnotationTypeMatcher{annotationType='Lcom/example/Component;'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=ExtendsTypeMatcher{className='com.example.Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=ImplementsTypeMatcher{interfaceName='com.example.Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=NameSuffixTypeMatcher{suffix='Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=RegexTypeMatcher{regex='.*Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=CustomTypeMatcher{}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseMatcher_WhenACustomTypeMatcherIsUsedWithAParameter() {
        parser.parseMatcher(context, tokens("matcher", NameSuffixTypeMatcher.class.getCanonicalName(), "Component"), new File("."));
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=NameSuffixTypeMatcher{suffix='Component'}, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=IncludeFullyQualifiedNameRegexFilter{regex='.*'}, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseFilter_WhenExcludeFullyQualifiedNameRegexTypeFilterIsUsed() {
        parser.parseFilter(context, tokens("filter", "exclude", "fqn-regex", ".*"), new File("."));
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=ExcludeFullyQualifiedNameRegexFilter{regex='.*'}, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
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
    void test_parseName_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseName(context, tokens("name"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: name <type-name|fqn|package>", e.getMessage());
        }
    }

    @Test
    void test_parseName() {
        parser.parseName(context, tokens("name", "type-name"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=null, supportingTypesStrategy=null, namingStrategy=TypeNamingStrategy{}, descriptionStrategy=null, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseDescription_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseDescription(context, tokens("description"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: description <first-sentence|truncated>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_FirstSentence() {
        parser.parseDescription(context, tokens("description", "first-sentence"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=FirstSentenceDescriptionStrategy{}, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseDescription_Truncated_ThrowsAnException_WhenNoMaxLengthIsSpecified() {
        try {
            parser.parseDescription(context, tokens("description", "truncated"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: description truncated <maxLength>", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_Truncated_ThrowsAnException_WhenAnInvalidMaxLengthIsSpecified() {
        try {
            parser.parseDescription(context, tokens("description", "truncated", "invalid"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Max length must be an integer", e.getMessage());
        }

        try {
            parser.parseDescription(context, tokens("description", "truncated", "-1"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Max length must be greater than 0", e.getMessage());
        }

        try {
            parser.parseDescription(context, tokens("description", "truncated", "0"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Max length must be greater than 0", e.getMessage());
        }
    }

    @Test
    void test_parseDescription_Truncated() {
        parser.parseDescription(context, tokens("description", "truncated", "50"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=TruncatedDescriptionStrategy{maxLength=50}, urlStrategy=null, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

    @Test
    void test_parseUrl_ThrowsAnException_WhenNoTypeIsSpecified() {
        try {
            parser.parseUrl(context, tokens("url"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: url <prefix-src>", e.getMessage());
        }
    }

    @Test
    void test_parseUrl_Prefix_ThrowsAnException_WhenNoPrefixIsSpecified() {
        try {
            parser.parseUrl(context, tokens("url", "prefix-src"), null);
            fail();
        } catch (Exception e) {
            assertEquals("Too few tokens, expected: url prefix-src <prefix>", e.getMessage());
        }
    }

    @Test
    void test_parseUrl_Prefix() {
        parser.parseUrl(context, tokens("url", "prefix-src", "https://example.com"), null);
        assertEquals("ComponentFinderStrategyBuilder{technology=null, typeMatcher=null, typeFilter=null, supportingTypesStrategy=null, namingStrategy=null, descriptionStrategy=null, urlStrategy=PrefixUrlStrategy{prefix='https://example.com/'}, componentVisitor=null}", context.getComponentFinderStrategyBuilder().toString());
    }

}