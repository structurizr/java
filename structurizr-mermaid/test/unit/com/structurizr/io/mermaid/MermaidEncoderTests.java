package com.structurizr.io.mermaid;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MermaidEncoderTests {

    @Test
    public void test_encode() throws Exception {
        assertEquals(
            "eyJjb2RlIjoiZ3JhcGggVERcbkFbQ2hyaXN0bWFzXSAtLT58R2V0IG1vbmV5fCBCKEdvIHNob3BwaW5nKVxuQiAtLT4gQ3tMZXQgbWUgdGhpbmt9XG5DIC0tPnxPbmV8IERbTGFwdG9wXVxuQyAtLT58VHdvfCBFW2lQaG9uZV1cbkMgLS0-fFRocmVlfCBGW2ZhOmZhLWNhciBDYXJdXG4iLCJtZXJtYWlkIjp7InRoZW1lIjoiZGVmYXVsdCJ9fQ",
            new MermaidEncoder().encode("graph TD\n" +
                    "A[Christmas] -->|Get money| B(Go shopping)\n" +
                    "B --> C{Let me think}\n" +
                    "C -->|One| D[Laptop]\n" +
                    "C -->|Two| E[iPhone]\n" +
                    "C -->|Three| F[fa:fa-car Car]\n"));
    }

}