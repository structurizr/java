package com.structurizr.api;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Md5DigestTests {

    private Md5Digest md5 = new Md5Digest();

    @Test
    public void test_generate_TreatsNullAsEmptyContent() throws Exception {
        assertEquals(md5.generate(null), md5.generate(""));
    }

    @Test
    public void test_generate() throws Exception {
        assertEquals("ed076287532e86365e841e92bfc50d8c", md5.generate("Hello World!"));
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", md5.generate(""));
    }

}