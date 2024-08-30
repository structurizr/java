package com.structurizr.dsl;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ThemeParserTests extends AbstractTests {

    private final ThemeParser parser = new ThemeParser();

    @Test
    void test_parseTheme_ThrowsAnException_WhenThereAreTooManyTokens() {
        try {
            parser.parseTheme(context(), null, tokens("theme", "url", "extra"));
            fail();
        } catch (Exception e) {
            assertEquals("Too many tokens, expected: theme <url|file>", e.getMessage());
        }
    }

    @Test
    void test_parseTheme_ThrowsAnException_WhenNoThemeIsSpecified() {
        try {
            parser.parseTheme(context(), null, tokens("theme"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: theme <url|file>", e.getMessage());
        }
    }

    @Test
    void test_parseTheme_AddsTheTheme_WhenAThemeIsSpecified() {
        parser.parseTheme(context(), null, tokens("theme", "http://example.com/1"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseTheme_AddsTheTheme_WhenTheDefaultThemeIsSpecified() {
        parser.parseTheme(context(), null, tokens("theme", "default"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("https://static.structurizr.com/themes/default/theme.json", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseThemes_ThrowsAnException_WhenNoThemesAreSpecified() {
        try {
            parser.parseThemes(context(), null, tokens("themes"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: themes <url|file> [url|file] ... [url|file]", e.getMessage());
        }
    }

    @Test
    void test_parseThemes_AddsTheTheme_WhenOneThemeIsSpecified() {
        parser.parseThemes(context(), null, tokens("themes", "http://example.com/1"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseThemes_AddsTheThemes_WhenMultipleThemesAreSpecified() {
        parser.parseThemes(context(), null, tokens("themes", "http://example.com/1", "http://example.com/2", "http://example.com/3"));

        assertEquals(3, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("http://example.com/1", workspace.getViews().getConfiguration().getThemes()[0]);
        assertEquals("http://example.com/2", workspace.getViews().getConfiguration().getThemes()[1]);
        assertEquals("http://example.com/3", workspace.getViews().getConfiguration().getThemes()[2]);
    }

    @Test
    void test_parseThemes_AddsTheTheme_WhenTheDefaultThemeIsSpecified() {
        parser.parseThemes(context(), null, tokens("themes", "default"));

        assertEquals(1, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("https://static.structurizr.com/themes/default/theme.json", workspace.getViews().getConfiguration().getThemes()[0]);
    }

    @Test
    void test_parseTheme_ThrowsAnException_WhenTheThemeFileDoesNotExist() {
        File dslFile = new File("src/test/resources/themes/workspace.dsl");
        try {
            parser.parseTheme(context(), dslFile, tokens("theme", "my-theme.json"));
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().endsWith("/src/test/resources/themes/my-theme.json does not exist"));
        }
    }

    @Test
    void test_parseTheme_ThrowsAnException_WhenTheThemeFileIsADirectory() {
        File dslFile = new File("src/test/resources/workspace.dsl");
        try {
            parser.parseTheme(context(), dslFile, tokens("theme", "themes"));
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().endsWith("/src/test/resources/themes is not a file"));
        }
    }

    @Test
    void test_parseTheme_InlinesTheTheme_WhenAThemeFileIsSpecified() {
        File dslFile = new File("src/test/resources/themes/workspace.dsl");
        parser.parseTheme(context(), dslFile, tokens("theme", "theme.json"));

        assertEquals(0, workspace.getViews().getConfiguration().getThemes().length);
        assertEquals("#ff0000", workspace.getViews().getConfiguration().getStyles().getElementStyle("Tag").getBackground());
        assertEquals("#00ff00", workspace.getViews().getConfiguration().getStyles().getRelationshipStyle("Tag").getColor());
    }

}