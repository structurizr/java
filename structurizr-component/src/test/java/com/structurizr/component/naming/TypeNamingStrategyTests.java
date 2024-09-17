package com.structurizr.component.naming;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeNamingStrategyTests {

    @Test
    void nameOf() {
        assertEquals("ClassName", new TypeNamingStrategy().nameOf(new Type("com.example.ClassName")));
    }

}