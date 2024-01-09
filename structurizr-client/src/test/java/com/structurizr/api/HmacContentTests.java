package com.structurizr.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HmacContentTests {


    @Test
    void toString_WhenThereAreNoStrings() {
        assertEquals("", new HmacContent().toString());
    }

    @Test
    void toString_WhenThereAreSomeStrings() {
        assertEquals("String1\nString2\nString3\n", new HmacContent("String1", "String2", "String3").toString());
    }

}
