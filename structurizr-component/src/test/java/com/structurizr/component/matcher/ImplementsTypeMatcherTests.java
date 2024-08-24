package com.structurizr.component.matcher;

import com.structurizr.component.Type;
import org.apache.bcel.classfile.ClassParser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ImplementsTypeMatcherTests {

    @Test
    void construction_ThrowsAnException_WhenPassedANullName() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ImplementsTypeMatcher(null));
    }

    @Test
    void construction_ThrowsAnException_WhenPassedAnEmptyName() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ImplementsTypeMatcher(""));
        assertThrowsExactly(IllegalArgumentException.class, () -> new ImplementsTypeMatcher(" "));
    }

    @Test
    void matches_ThrowsAnException_WhenPassedNull() {
        assertThrowsExactly(IllegalArgumentException.class, () -> new ImplementsTypeMatcher("com.example.InterfaceName").matches(null));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoUnderlyingJavaClass() {
        Type type = new Type("com.structurizr.component.matcher.implementsTypeMatcher.CustomerController");

        assertFalse(new ImplementsTypeMatcher("com.structurizr.component.matcher.implementsTypeMatcher.Controller").matches(type));
    }

    @Test
    void matches_ReturnsFalse_WhenThereIsNoMatch() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/matcher/implementsTypeMatcher/CustomerController.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        assertFalse(new ImplementsTypeMatcher("com.structurizr.component.matcher.implementsTypeMatcher.Repository").matches(type));
    }

    @Test
    void matches_ReturnsTrue_WhenThereIsAMatch() throws Exception {
        File classes = new File("build/classes/java/test");
        ClassParser parser = new ClassParser(new File(classes, "com/structurizr/component/matcher/implementsTypeMatcher/CustomerController.class").getAbsolutePath());
        Type type = new Type(parser.parse());

        assertTrue(new ImplementsTypeMatcher("com.structurizr.component.matcher.implementsTypeMatcher.Controller").matches(type));
    }

}