package com.structurizr.dsl;

import com.structurizr.util.StringUtils;

class GroupParser {

    private static final String STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME = "structurizr.groupSeparator";

    private static final String GRAMMAR = "group <name> {";

    private final static int NAME_INDEX = 1;
    private final static int BRACE_INDEX = 2;

    ElementGroup parse(GroupableDslContext dslContext, Tokens tokens) {
        // group <name> {

        if (tokens.hasMoreThan(BRACE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR);
        }

        if (!tokens.includes(BRACE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        if (!DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(tokens.get(BRACE_INDEX))) {
            throw new RuntimeException("Expected: " + GRAMMAR);
        }

        ElementGroup group;
        if (dslContext.hasGroup()) {
            String groupSeparator = ((DslContext)dslContext).getWorkspace().getModel().getProperties().getOrDefault(STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME, "");

            if (StringUtils.isNullOrEmpty(groupSeparator)) {
                throw new RuntimeException("To use nested groups, please define a model property named " + STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME);
            }

            group = new ElementGroup(tokens.get(NAME_INDEX), groupSeparator, dslContext.getGroup());
        } else {
            group = new ElementGroup(tokens.get(NAME_INDEX));
        }

        return group;
    }

}