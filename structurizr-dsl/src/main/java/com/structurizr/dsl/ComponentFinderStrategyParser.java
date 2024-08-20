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

final class ComponentFinderStrategyParser extends AbstractParser {

    private static final String MATCHER_GRAMMAR = "matcher <annotation|extends|implements|namesuffix|regex> [parameters]";
    private static final String FILTER_GRAMMAR = "filter <includeregex|excluderegex> [parameters]";
    private static final String SUPPORTING_TYPES_GRAMMAR = "supportingTypes <referenced|referencedinpackage|inpackage|underpackage> [parameters]";
    private static final String NAMING_GRAMMAR = "naming <name|fqn|package>";

    void parseMatcher(ComponentFinderStrategyDslContext context, Tokens tokens) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + MATCHER_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case "annotation":
                if (tokens.size() == 4) {
                    String name = tokens.get(2);
                    String technology = tokens.get(3);

                    context.getComponentFinderStrategyBuilder().matchedBy(new AnnotationTypeMatcher(name, technology));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_GRAMMAR);
                }
                break;
            case "extends":
                if (tokens.size() == 4) {
                    String name = tokens.get(2);
                    String technology = tokens.get(3);

                    context.getComponentFinderStrategyBuilder().matchedBy(new ExtendsTypeMatcher(name, technology));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_GRAMMAR);
                }
                break;
            case "implements":
                if (tokens.size() == 4) {
                    String name = tokens.get(2);
                    String technology = tokens.get(3);

                    context.getComponentFinderStrategyBuilder().matchedBy(new ImplementsTypeMatcher(name, technology));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_GRAMMAR);
                }
                break;
            case "namesuffix":
                if (tokens.size() == 4) {
                    String suffix = tokens.get(2);
                    String technology = tokens.get(3);

                    context.getComponentFinderStrategyBuilder().matchedBy(new NameSuffixTypeMatcher(suffix, technology));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_GRAMMAR);
                }
                break;
            case "regex":
                if (tokens.size() == 4) {
                    String regex = tokens.get(2);
                    String technology = tokens.get(3);

                    context.getComponentFinderStrategyBuilder().matchedBy(new RegexTypeMatcher(regex, technology));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_GRAMMAR);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown matcher: " + type);
        }
    }

    void parseFilter(ComponentFinderStrategyDslContext context, Tokens tokens) {
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

    void parseSupportingTypes(ComponentFinderStrategyDslContext context, Tokens tokens) {
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

    void parseNaming(ComponentFinderStrategyDslContext context, Tokens tokens) {
        if (tokens.size() < 1) {
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