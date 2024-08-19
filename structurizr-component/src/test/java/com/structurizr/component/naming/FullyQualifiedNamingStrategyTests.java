package com.structurizr.component.naming;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullyQualifiedNamingStrategyTests {

    @Test
    void nameOf() {
        assertEquals("com.example.ClassName", new FullyQualifiedNamingStrategy().nameOf(new Type("com.example.ClassName")));
    }

}