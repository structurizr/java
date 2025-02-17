package com.structurizr.dsl;

import com.structurizr.model.Component;
import com.structurizr.util.StringUtils;

class GroupParser {

    private static final String STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME = "structurizr.groupSeparator";

    private static final String GRAMMAR_AS_CONTEXT = "group <name> {";
    private static final String GRAMMAR_AS_PROPERTY = "group <name>";

    private final static int NAME_INDEX = 1;
    private final static int BRACE_INDEX = 2;


    ElementGroup parseContext(GroupableDslContext dslContext, Tokens tokens) {
        // group <name> {

        if (tokens.hasMoreThan(BRACE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR_AS_CONTEXT);
        }

        if (!tokens.includes(BRACE_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR_AS_CONTEXT);
        }

        if (!DslContext.CONTEXT_START_TOKEN.equalsIgnoreCase(tokens.get(BRACE_INDEX))) {
            throw new RuntimeException("Expected: " + GRAMMAR_AS_CONTEXT);
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

    void parseProperty(ComponentDslContext dslContext, Tokens tokens) {
        // group <name>

        if (tokens.includes(BRACE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + GRAMMAR_AS_PROPERTY);
        }

        if (!tokens.includes(NAME_INDEX)) {
            throw new RuntimeException("Expected: " + GRAMMAR_AS_PROPERTY);
        }

        String group = tokens.get(NAME_INDEX);

        Component component = dslContext.getComponent();
        String existingGroup = component.getGroup();

        if (!StringUtils.isNullOrEmpty(existingGroup)) {
            String groupSeparator = dslContext.getWorkspace().getModel().getProperties().getOrDefault(STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME, "");
            if (StringUtils.isNullOrEmpty(groupSeparator)) {
                throw new RuntimeException("To use nested groups, please define a model property named " + STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME);
            }

            group = existingGroup + groupSeparator + group;
        }

        component.setGroup(group);
    }

}