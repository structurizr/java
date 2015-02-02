package com.structurizr.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HmacContentTests {

    private HmacContent content;

    @Test
    public void test_toString_WhenThereAreNoStrings() {
        assertEquals("", new HmacContent().toString());
    }

    @Test
    public void test_toString_WhenThereAreSomeStrings() {
        assertEquals("String1\nString2\nString3\n", new HmacContent("String1", "String2", "String3").toString());
    }

}
