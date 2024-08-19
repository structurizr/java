package com.structurizr.component.naming;

import com.structurizr.component.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultNamingStrategyTests {

    @Test
    void nameOf() {
        assertEquals("Class Name", new DefaultNamingStrategy().nameOf(new Type("com.example.ClassName")));
    }

}