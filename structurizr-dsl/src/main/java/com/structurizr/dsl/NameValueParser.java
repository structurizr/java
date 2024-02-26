package com.structurizr.dsl;

final class NameValueParser extends AbstractParser {

    private static final String GRAMMAR = "%s <name> <value>";

    private static final int KEYWORD_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int VALUE_INDEX = 2;

    private static final String NAME_REGEX = "[a-zA-Z0-9-_.]+";

    NameValuePair parseConstant(Tokens tokens) {
        NameValuePair nvp = parse(tokens);
        nvp.setType(NameValueType.Constant);

        return nvp;
    }

    NameValuePair parseVariable(Tokens tokens) {
        NameValuePair nvp = parse(tokens);
        nvp.setType(NameValueType.Variable);

        return nvp;
    }

    private NameValuePair parse(Tokens tokens) {
        // !const name value
        // !var name value

        if (tokens.hasMoreThan(VALUE_INDEX)) {
            throw new RuntimeException("Too many tokens, expected: " + String.format(GRAMMAR, tokens.get(KEYWORD_INDEX)));
        }

        if (!tokens.includes(VALUE_INDEX)) {
            throw new RuntimeException("Expected: " + String.format(GRAMMAR, tokens.get(KEYWORD_INDEX)));
        }

        String name = tokens.get(NAME_INDEX);
        String value = tokens.get(VALUE_INDEX);

        if (!name.matches(NAME_REGEX)) {
            throw new RuntimeException("Constant/variable names must only contain the following characters: a-zA-Z0-9-_.");
        }

        return new NameValuePair(name, value);
    }

}