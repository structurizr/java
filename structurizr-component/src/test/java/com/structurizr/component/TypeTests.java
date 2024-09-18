package com.structurizr.component;

import com.structurizr.component.matcher.ImplementsTypeMatcher;
import org.apache.bcel.classfile.ClassParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

public class TypeTests {

    private Type type;

    @Test
    void name() {
        type = new Type("com.example.ClassName");
        assertEquals("ClassName", type.getName());
    }

    @Test
    void packageName() {
        type = new Type("com.example.ClassName");
        assertEquals("com.example", type.getPackageName());
    }

    @Test
    void getTags_WhenTypeHasOneTag() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/types/TypeWithTag.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        List<String> tags = type.getTags();
        assertEquals(1, tags.size());
        assertTrue(tags.contains("Tag 1"));
    }

    @Test
    void getTags_WhenTypeHasManyTags() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/types/TypeWithTags.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        List<String> tags = type.getTags();
        assertEquals(3, tags.size());
        assertEquals("Tag 1", tags.get(0));
        assertEquals("Tag 2", tags.get(1));
        assertEquals("Tag 3", tags.get(2));
    }

    @Test
    void getTags_WhenTypeHasOneProperty() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/types/TypeWithProperty.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        Map<String, String> properties = type.getProperties();
        assertEquals(1, properties.size());
        assertEquals("Value", properties.get("Name"));
    }

    @Test
    void getTags_WhenTypeHasManyProperties() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/types/TypeWithProperties.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        Map<String, String> properties = type.getProperties();
        assertEquals(3, properties.size());
        assertEquals("Value1", properties.get("Name1"));
        assertEquals("Value2", properties.get("Name2"));
        assertEquals("Value3", properties.get("Name3"));
    }

}