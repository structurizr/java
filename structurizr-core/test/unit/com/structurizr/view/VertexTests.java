package com.structurizr.view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class VertexTests {

    @Test
    void test_equals() {
        Vertex vertex1 = new Vertex(123, 456);
        Vertex vertex2 = new Vertex(123, 456);
        Vertex vertex3 = new Vertex(456, 123);

        assertNotEquals(vertex1, null);
        assertNotEquals(vertex1, "hello world");

        assertEquals(vertex1, vertex1);
        assertEquals(vertex1, vertex2);
        assertEquals(vertex2, vertex1);
        assertNotEquals(vertex2, vertex3);
    }

}