package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ExtendsTypeMatcherTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullName() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExtendsTypeMatcher(null, "Technology"));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptyName() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExtendsTypeMatcher("", "Technology"));
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExtendsTypeMatcher(" ", "Technology"));
    }

    @Test
    void matches_ThrowsAnException_WhenPassedNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ExtendsTypeMatcher("com.example.ClassName", "Technology").matches(null));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoUnderlyingJavaClass() {
        Type type = new Type("com.structurizr.component.matcher.extendsTypeMatcher.CustomerController");

        assertFalse(new ExtendsTypeMatcher("com.structurizr.component.matcher.extendsTypeMatcher.AbstractController", "Technology").matches(type));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoMatch() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/matcher/extendsTypeMatcher/CustomerController.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        assertFalse(new ExtendsTypeMatcher("com.structurizr.component.matcher.extendsTypeMatcher.AbstractRepository", "Technology").matches(type));
    }

    @Test
    void matches_ReturnsTrue_WhenThereIsAMatch() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/matcher/extendsTypeMatcher/CustomerController.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        assertTrue(new ExtendsTypeMatcher("com.structurizr.component.matcher.extendsTypeMatcher.AbstractController", "Technology").matches(type));
    }

}