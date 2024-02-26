package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class NameValueParserTests extends AbstractTests {

    private final NameValueParser parser = new NameValueParser();

    @Test
    void test_parseConstant_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseConstant(tokens("!const", "name", "value", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !const <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parseConstant_ThrowsAnException_WhenNoNameOrValueIsSpecified() {
        try {
            parser.parseConstant(tokens("!const"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !const <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parseConstant_ThrowsAnException_WhenNoValueIsSpecified() {
        try {
            parser.parseConstant(tokens("!const", "name"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !const <name> <value>", e.getMessage());
        }
    }

    @Test
    void test_parseConstant_ThrowsAnException_WhenNameContainsDisallowedCharacters() {
        try {
            parser.parseConstant(tokens("!const", "${NAME}", "value"));
            fail();
        } catch (Exception e) {
            assertEquals("Constant/variable names must only contain the following characters: a-zA-Z0-9-_.", e.getMessage());
        }
    }

    @Test
    void test_parseConstant_CreatesAConstant() {
        NameValuePair nameValuePair = parser.parseConstant(tokens("!const", "name", "value"));
        assertEquals("name", nameValuePair.getName());
        assertEquals("value", nameValuePair.getValue());
        assertEquals(NameValueType.Constant, nameValuePair.getType());
    }

    @Test
    void test_parseVariable_CreatesAVariable() {
        NameValuePair nameValuePair = parser.parseVariable(tokens("!var", "name", "value"));
        assertEquals("name", nameValuePair.getName());
        assertEquals("value", nameValuePair.getValue());
        assertEquals(NameValueType.Variable, nameValuePair.getType());
    }

}