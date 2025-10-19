package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class IncludeParserTests extends AbstractTests {

    private final IncludeParser parser = new IncludeParser();

    @Test
    void test_parse_ThrowsAnException_WhenTheIncludeFeatureIsNotEnabled() {
        try {
            DslContext context = context();
            context.getFeatures().disable(Features.INCLUDE);
            parser.parse(context, null, tokens("!include", "file", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("!include is not permitted (feature structurizr.feature.dsl.include is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            DslContext context = context();
            context.getFeatures().enable(Features.INCLUDE);
            parser.parse(context, null, tokens("!include", "file", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: !include <file|directory|url>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenAFileIsNotSpecified() {
        try {
            DslContext context = context();
            context.getFeatures().enable(Features.INCLUDE);
            parser.parse(context, null, tokens("!include"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: !include <file|directory|url>", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenTheFileSystemAccessFeatureIsNotEnabled() {
        try {
            DslContext context = context();
            context.getFeatures().enable(Features.INCLUDE);
            context.getFeatures().disable(Features.FILE_SYSTEM);
            parser.parse(context, null, tokens("!include", "file"));
            fail();
        } catch (Exception e) {
            assertEquals("!include <file> is not permitted (feature structurizr.feature.dsl.filesystem is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenIncludingFromHttpsAndHttpsIsNotEnabled() {
        try {
            DslContext context = context();
            context.getFeatures().enable(Features.INCLUDE);
            context.getFeatures().disable(Features.HTTPS);
            parser.parse(context, null, tokens("!include", "https://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Includes via HTTPS are not permitted (feature structurizr.feature.dsl.https is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parse_ThrowsAnException_WhenIncludingFromHttpAndHttpIsNotEnabled() {
        try {
            DslContext context = context();
            context.getFeatures().enable(Features.INCLUDE);
            context.getFeatures().disable(Features.HTTP);
            parser.parse(context, null, tokens("!include", "http://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Includes via HTTP are not permitted (feature structurizr.feature.dsl.http is not enabled)", e.getMessage());
        }
    }

}