package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AbstractViewParserTests {

    private final AbstractViewParser parser = new SystemLandscapeViewParser();

    @Test
    void test_validateViewKey() {
        parser.validateViewKey("key");
        parser.validateViewKey("key123");
        parser.validateViewKey("Key123");
        parser.validateViewKey("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_-");

        try {
            parser.validateViewKey("abcdefghijklmnopqrstuvwxyz/ABCDEFGHIJKLMNOPQRSTUVWXYZ/0123456789");
            fail();
        } catch (Exception e) {
            assertEquals("View keys can only contain the following characters: a-zA-Z0-9_-", e.getMessage());
        }

        try {
            parser.validateViewKey("abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789");
            fail();
        } catch (Exception e) {
            assertEquals("View keys can only contain the following characters: a-zA-Z0-9_-", e.getMessage());
        }
    }

}