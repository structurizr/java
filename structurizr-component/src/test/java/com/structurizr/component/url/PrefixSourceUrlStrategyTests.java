package com.structurizr.component.url;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrefixSourceUrlStrategyTests {

    @Test
    void test_urlOf_WhenTheSourceUsesForwardSlashFileSeparators() {
        Type type = new Type("com.example.ClassName");
        type.setSource("com/example/ClassName.java");

        assertEquals("https://example.com/src/main/java/com/example/ClassName.java", new PrefixSourceUrlStrategy("https://example.com/src/main/java").urlOf(type));
    }

    @Test
    void test_urlOf_WhenTheSourceUsesBackslashFileSeparators() {
        Type type = new Type("com.example.ClassName");
        type.setSource("com\\example\\ClassName.java");

        assertEquals("https://example.com/src/main/java/com/example/ClassName.java", new PrefixSourceUrlStrategy("https://example.com/src/main/java").urlOf(type));
    }

}