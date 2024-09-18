package com.structurizr.dsl;

import com.structurizr.component.filter.ExcludeFullyQualifiedNameRegexFilter;
import com.structurizr.component.filter.IncludeFullyQualifiedNameRegexFilter;

final class ComponentFinderParser extends AbstractParser {

    private static final String CLASSES_GRAMMAR = "classes <path>";
    private static final String SOURCE_GRAMMAR = "source <path>";

    private static final String FILTER_INCLUDE = "include";
    private static final String FILTER_EXCLUDE = "exclude";
    private static final String FILTER_FQN_REGEX = "fqn-regex";
    private static final String FILTER_GRAMMAR = "filter <" + FILTER_INCLUDE + "|" + FILTER_EXCLUDE + "> <" + FILTER_FQN_REGEX + "> [parameters]";

    void parseClasses(ComponentFinderDslContext context, Tokens tokens) {
        // classes <path>

        if (tokens.hasMoreThan(1)) {
            throw new RuntimeException("Too many tokens, expected: " + CLASSES_GRAMMAR);
        }

        context.getComponentFinderBuilder().fromClasses(tokens.get(1));
    }

    void parseSource(ComponentFinderDslContext context, Tokens tokens) {
        // source <path>

        if (tokens.hasMoreThan(1)) {
            throw new RuntimeException("Too many tokens, expected: " + SOURCE_GRAMMAR);
        }

        context.getComponentFinderBuilder().fromSource(tokens.get(1));
    }

    void parseFilter(ComponentFinderDslContext context, Tokens tokens) {
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
                        context.getComponentFinderBuilder().filteredBy(new IncludeFullyQualifiedNameRegexFilter(regex));
                    } else {
                        context.getComponentFinderBuilder().filteredBy(new ExcludeFullyQualifiedNameRegexFilter(regex));
                    }
                } else {
                    throw new RuntimeException("Expected: " + FILTER_GRAMMAR);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown filter: " + type);
        }
    }

}