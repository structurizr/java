package com.structurizr.dsl;

import com.structurizr.component.filter.ExcludeTypesByRegexFilter;
import com.structurizr.component.filter.IncludeTypesByRegexFilter;
import com.structurizr.component.matcher.*;
import com.structurizr.component.naming.DefaultPackageNamingStrategy;
import com.structurizr.component.naming.SimpleNamingStrategy;
import com.structurizr.component.naming.FullyQualifiedNamingStrategy;
import com.structurizr.component.supporting.AllReferencedTypesInPackageSupportingTypesStrategy;
import com.structurizr.component.supporting.AllReferencedTypesSupportingTypesStrategy;
import com.structurizr.component.supporting.AllTypesInPackageSupportingTypesStrategy;
import com.structurizr.component.supporting.AllTypesUnderPackageSupportingTypesStrategy;

import java.io.File;
import java.lang.reflect.Constructor;

final class ComponentFinderStrategyParser extends AbstractParser {

    private static final String TECHNOLOGY_GRAMMAR = "technology <name>";

    private static final String MATCHER_GRAMMAR = "matcher <annotation|extends|implements|namesuffix|regex> [parameters]";
    private static final String MATCHER_ANNOTATION_GRAMMAR = "matcher annotation <fqn>";
    private static final String MATCHER_EXTENDS_GRAMMAR = "matcher extends <fqn>";
    private static final String MATCHER_IMPLEMENTS_GRAMMAR = "matcher implements <fqn>";
    private static final String MATCHER_NAMESUFFIX_GRAMMAR = "matcher namesuffix <name>";
    private static final String MATCHER_REGEX_GRAMMAR = "matcher regex <regex>";

    private static final String FILTER_GRAMMAR = "filter <includeregex|excluderegex> [parameters]";
    private static final String SUPPORTING_TYPES_GRAMMAR = "supportingTypes <referenced|referencedinpackage|inpackage|underpackage> [parameters]";
    private static final String NAMING_GRAMMAR = "naming <name|fqn|package>";

    void parseTechnology(ComponentFinderStrategyDslContext context, Tokens tokens) {
        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: " + TECHNOLOGY_GRAMMAR);
        }

        String name = tokens.get(1);
        context.getComponentFinderStrategyBuilder().forTechnology(name);
    }

    void parseMatcher(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + MATCHER_GRAMMAR);
        }

        String type = tokens.get(1);
        switch (type.toLowerCase()) {
            case "annotation":
                if (tokens.size() == 3) {
                    String name = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new AnnotationTypeMatcher(name));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_ANNOTATION_GRAMMAR);
                }
                break;
            case "extends":
                if (tokens.size() == 3) {
                    String name = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new ExtendsTypeMatcher(name));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_EXTENDS_GRAMMAR);
                }
                break;
            case "implements":
                if (tokens.size() == 3) {
                    String name = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new ImplementsTypeMatcher(name));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_IMPLEMENTS_GRAMMAR);
                }
                break;
            case "namesuffix":
                if (tokens.size() == 3) {
                    String suffix = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new NameSuffixTypeMatcher(suffix));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_NAMESUFFIX_GRAMMAR);
                }
                break;
            case "regex":
                if (tokens.size() == 3) {
                    String regex = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new RegexTypeMatcher(regex));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_REGEX_GRAMMAR);
                }
                break;
            default:
                try {
                    Class<? extends TypeMatcher> typeMatcherClass = context.loadClass(type, dslFile);

                    TypeMatcher typeMatcher;
                    if (tokens.size() == 3) {
                        String parameter = tokens.get(2);
                        typeMatcher = typeMatcherClass.getDeclaredConstructor(String.class).newInstance(parameter);
                    } else {
                        typeMatcher = typeMatcherClass.getDeclaredConstructor().newInstance();
                    }

                    context.getComponentFinderStrategyBuilder().matchedBy(typeMatcher);
                } catch (Exception e) {
                    throw new RuntimeException("Type matcher \"" + type + "\" could not be loaded - " + e.getClass() + ": " + e.getMessage());
                }
        }
    }

    void parseFilter(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + FILTER_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case "includeregex":
                if (tokens.size() == 3) {
                    String regex = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().filteredBy(new IncludeTypesByRegexFilter(regex));
                } else {
                    throw new RuntimeException("Expected: " + FILTER_GRAMMAR);
                }
                break;
            case "excluderegex":
                if (tokens.size() == 3) {
                    String regex = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().filteredBy(new ExcludeTypesByRegexFilter(regex));
                } else {
                    throw new RuntimeException("Expected: " + FILTER_GRAMMAR);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown filter: " + type);
        }
    }

    void parseSupportingTypes(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + SUPPORTING_TYPES_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case "referenced":
                context.getComponentFinderStrategyBuilder().supportedBy(new AllReferencedTypesSupportingTypesStrategy());
                break;
            case "referencedinpackage":
                context.getComponentFinderStrategyBuilder().supportedBy(new AllReferencedTypesInPackageSupportingTypesStrategy());
                break;
            case "inpackage":
                context.getComponentFinderStrategyBuilder().supportedBy(new AllTypesInPackageSupportingTypesStrategy());
                break;
            case "underpackage":
                context.getComponentFinderStrategyBuilder().supportedBy(new AllTypesUnderPackageSupportingTypesStrategy());
                break;
            default:
                throw new IllegalArgumentException("Unknown supporting types strategy: " + type);
        }
    }

    void parseNaming(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + NAMING_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case "name":
                context.getComponentFinderStrategyBuilder().namedBy(new SimpleNamingStrategy());
                break;
            case "fqn":
                context.getComponentFinderStrategyBuilder().namedBy(new FullyQualifiedNamingStrategy());
                break;
            case "package":
                context.getComponentFinderStrategyBuilder().namedBy(new DefaultPackageNamingStrategy());
                break;
            default:
                throw new IllegalArgumentException("Unknown naming strategy: " + type);
        }
    }

}