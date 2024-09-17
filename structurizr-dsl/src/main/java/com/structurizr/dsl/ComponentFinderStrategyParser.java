package com.structurizr.dsl;

import com.structurizr.component.description.FirstSentenceDescriptionStrategy;
import com.structurizr.component.description.TruncatedDescriptionStrategy;
import com.structurizr.component.filter.ExcludeTypesByRegexFilter;
import com.structurizr.component.filter.IncludeTypesByRegexFilter;
import com.structurizr.component.matcher.*;
import com.structurizr.component.naming.DefaultPackageNamingStrategy;
import com.structurizr.component.naming.TypeNamingStrategy;
import com.structurizr.component.naming.FullyQualifiedNamingStrategy;
import com.structurizr.component.supporting.*;
import com.structurizr.component.url.PrefixUrlStrategy;

import java.io.File;
import java.util.List;

final class ComponentFinderStrategyParser extends AbstractParser {

    private static final String TECHNOLOGY_GRAMMAR = "technology <name>";

    private static final String MATCHER_ANNOTATION = "annotation";
    private static final String MATCHER_EXTENDS = "extends";
    private static final String MATCHER_IMPLEMENTS = "implements";
    private static final String MATCHER_NAME_SUFFIX = "name-suffix";
    private static final String MATCHER_FQN_REGEX = "fqn-regex";
    private static final String MATCHER_GRAMMAR = "matcher <" + String.join("|", List.of(MATCHER_ANNOTATION, MATCHER_EXTENDS, MATCHER_IMPLEMENTS, MATCHER_NAME_SUFFIX, MATCHER_FQN_REGEX)) + "> [parameters]";
    private static final String MATCHER_ANNOTATION_GRAMMAR = "matcher annotation <fqn>";
    private static final String MATCHER_EXTENDS_GRAMMAR = "matcher extends <fqn>";
    private static final String MATCHER_IMPLEMENTS_GRAMMAR = "matcher implements <fqn>";
    private static final String MATCHER_NAMESUFFIX_GRAMMAR = "matcher name-suffix <name>";
    private static final String MATCHER_REGEX_GRAMMAR = "matcher fqn-regex <regex>";

    private static final String FILTER_INCLUDE = "include";
    private static final String FILTER_EXCLUDE = "exclude";
    private static final String FILTER_FQN_REGEX = "fqn-regex";
    private static final String FILTER_GRAMMAR = "filter <" + FILTER_INCLUDE + "|" + FILTER_EXCLUDE + "> <" + FILTER_FQN_REGEX + "> [parameters]";

    private static final String SUPPORTING_TYPES_ALL_REFERENCED = "all-referenced";
    private static final String SUPPORTING_TYPES_REFERENCED_IN_PACKAGE = "referenced-in-package";
    private static final String SUPPORTING_TYPES_IN_PACKAGE = "in-package";
    private static final String SUPPORTING_TYPES_UNDER_PACKAGE = "under-package";
    private static final String SUPPORTING_TYPES_NONE = "none";
    private static final String SUPPORTING_TYPES_GRAMMAR = "supportingTypes <" + String.join("|", List.of(SUPPORTING_TYPES_ALL_REFERENCED, SUPPORTING_TYPES_REFERENCED_IN_PACKAGE, SUPPORTING_TYPES_IN_PACKAGE, SUPPORTING_TYPES_UNDER_PACKAGE, SUPPORTING_TYPES_NONE)) + "> [parameters]";

    private static final String NAME_TYPE_NAME = "type-name";
    private static final String NAME_FQN = "fqn";
    private static final String NAME_PACKAGE = "package";
    private static final String NAME_GRAMMAR = "name <" + String.join("|", List.of(NAME_TYPE_NAME, NAME_FQN, NAME_PACKAGE)) + ">";

    private static final String DESCRIPTION_TRUNCATED = "truncated";
    private static final String DESCRIPTION_FIRST_SENTENCE = "first-sentence";
    private static final String DESCRIPTION_GRAMMAR = "description <" + String.join("|", List.of(DESCRIPTION_FIRST_SENTENCE, DESCRIPTION_TRUNCATED)) + ">";
    private static final String DESCRIPTION_TRUNCATED_GRAMMAR = "description truncated <maxLength>";

    private static final String URL_PREFIX = "prefix";
    private static final String URL_GRAMMAR = "url <" + String.join("|", List.of(URL_PREFIX)) + ">";
    private static final String URL_PREFIX_GRAMMAR = "url prefix <prefix>";

    void parseTechnology(ComponentFinderStrategyDslContext context, Tokens tokens) {
        if (tokens.size() != 2) {
            throw new RuntimeException("Expected: " + TECHNOLOGY_GRAMMAR);
        }

        String name = tokens.get(1);
        context.getComponentFinderStrategyBuilder().withTechnology(name);
    }

    void parseMatcher(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + MATCHER_GRAMMAR);
        }

        String type = tokens.get(1);
        switch (type.toLowerCase()) {
            case MATCHER_ANNOTATION:
                if (tokens.size() == 3) {
                    String name = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new AnnotationTypeMatcher(name));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_ANNOTATION_GRAMMAR);
                }
                break;
            case MATCHER_EXTENDS:
                if (tokens.size() == 3) {
                    String name = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new ExtendsTypeMatcher(name));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_EXTENDS_GRAMMAR);
                }
                break;
            case MATCHER_IMPLEMENTS:
                if (tokens.size() == 3) {
                    String name = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new ImplementsTypeMatcher(name));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_IMPLEMENTS_GRAMMAR);
                }
                break;
            case MATCHER_NAME_SUFFIX:
                if (tokens.size() == 3) {
                    String suffix = tokens.get(2);

                    context.getComponentFinderStrategyBuilder().matchedBy(new NameSuffixTypeMatcher(suffix));
                } else {
                    throw new RuntimeException("Expected: " + MATCHER_NAMESUFFIX_GRAMMAR);
                }
                break;
            case MATCHER_FQN_REGEX:
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
        if (tokens.size() < 3) {
            throw new RuntimeException("Too few tokens, expected: " + FILTER_GRAMMAR);
        }

        String includeOrExclude = tokens.get(1).toLowerCase();
        if (!"include".equalsIgnoreCase(includeOrExclude) && !"exclude".equalsIgnoreCase(includeOrExclude)) {
            throw new RuntimeException("Filter mode should be \"" + FILTER_INCLUDE + "\" or \"" + FILTER_EXCLUDE + "\": " + FILTER_GRAMMAR);
        }

        String type = tokens.get(2).toLowerCase();
        switch (type) {
            case FILTER_FQN_REGEX:
                if (tokens.size() == 4) {
                    String regex = tokens.get(3);

                    if (FILTER_INCLUDE.equalsIgnoreCase(includeOrExclude)) {
                        context.getComponentFinderStrategyBuilder().filteredBy(new IncludeTypesByRegexFilter(regex));
                    } else {
                        context.getComponentFinderStrategyBuilder().filteredBy(new ExcludeTypesByRegexFilter(regex));
                    }
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
            case SUPPORTING_TYPES_ALL_REFERENCED:
                context.getComponentFinderStrategyBuilder().supportedBy(new AllReferencedTypesSupportingTypesStrategy());
                break;
            case SUPPORTING_TYPES_REFERENCED_IN_PACKAGE:
                context.getComponentFinderStrategyBuilder().supportedBy(new AllReferencedTypesInPackageSupportingTypesStrategy());
                break;
            case SUPPORTING_TYPES_IN_PACKAGE:
                context.getComponentFinderStrategyBuilder().supportedBy(new AllTypesInPackageSupportingTypesStrategy());
                break;
            case SUPPORTING_TYPES_UNDER_PACKAGE:
                context.getComponentFinderStrategyBuilder().supportedBy(new AllTypesUnderPackageSupportingTypesStrategy());
                break;
            case SUPPORTING_TYPES_NONE:
                context.getComponentFinderStrategyBuilder().supportedBy(new DefaultSupportingTypesStrategy());
                break;
            default:
                throw new IllegalArgumentException("Unknown supporting types strategy: " + type);
        }
    }

    void parseName(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + NAME_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case NAME_TYPE_NAME:
                context.getComponentFinderStrategyBuilder().withName(new TypeNamingStrategy());
                break;
            case NAME_FQN:
                context.getComponentFinderStrategyBuilder().withName(new FullyQualifiedNamingStrategy());
                break;
            case NAME_PACKAGE:
                context.getComponentFinderStrategyBuilder().withName(new DefaultPackageNamingStrategy());
                break;
            default:
                throw new IllegalArgumentException("Unknown name strategy: " + type);
        }
    }

    void parseDescription(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + DESCRIPTION_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case DESCRIPTION_FIRST_SENTENCE:
                context.getComponentFinderStrategyBuilder().withDescription(new FirstSentenceDescriptionStrategy());
                break;
            case DESCRIPTION_TRUNCATED:
                if (tokens.size() < 3) {
                    throw new RuntimeException("Too few tokens, expected: " + DESCRIPTION_TRUNCATED_GRAMMAR);
                }

                try {
                    int maxLength = Integer.parseInt(tokens.get(2));
                    context.getComponentFinderStrategyBuilder().withDescription(new TruncatedDescriptionStrategy(maxLength));
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Max length must be an integer");
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown description strategy: " + type);
        }
    }

    void parseUrl(ComponentFinderStrategyDslContext context, Tokens tokens, File dslFile) {
        if (tokens.size() < 2) {
            throw new RuntimeException("Too few tokens, expected: " + URL_GRAMMAR);
        }

        String type = tokens.get(1).toLowerCase();
        switch (type) {
            case URL_PREFIX:
                if (tokens.size() < 3) {
                    throw new RuntimeException("Too few tokens, expected: " + URL_PREFIX_GRAMMAR);
                }

                String prefix = tokens.get(2);
                context.getComponentFinderStrategyBuilder().withUrl(new PrefixUrlStrategy(prefix));
                break;
            default:
                throw new IllegalArgumentException("Unknown URL strategy: " + type);
        }
    }

}