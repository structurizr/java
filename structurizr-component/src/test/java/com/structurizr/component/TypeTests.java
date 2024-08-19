package com.structurizr.component;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

}