package com.structurizr.view;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VertexTests {

    @Test
    public void test_equals() {
        Vertex vertex1 = new Vertex(123, 456);
        Vertex vertex2 = new Vertex(123, 456);
        Vertex vertex3 = new Vertex(456, 123);

        assertFalse(vertex1.equals(null));
        assertFalse(vertex1.equals("hello world"));

        assertTrue(vertex1.equals(vertex1));
        assertTrue(vertex1.equals(vertex2));
        assertTrue(vertex2.equals(vertex1));
        assertFalse(vertex2.equals(vertex3));
    }

}