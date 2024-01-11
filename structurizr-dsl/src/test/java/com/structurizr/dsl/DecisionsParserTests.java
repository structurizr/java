package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class DecisionsParserTests extends AbstractTests {

    private final DecisionsParser parser = new DecisionsParser();

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parse(new WorkspaceDslContext(), null, tokens("decisions", "path", "fqn", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !decisions <path> <type|fqn>", e.getMessage());
        }
    }

}