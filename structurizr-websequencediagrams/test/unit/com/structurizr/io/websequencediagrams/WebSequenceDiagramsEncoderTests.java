package com.structurizr.io.websequencediagrams;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

public class WebSequenceDiagramsEncoderTests {

    @Test
    @Ignore
    public void test_encode() throws Exception {
        // this test does not work, and requires a proper implementation of the algorithm used to encode diagram definitions
        // TODO
        //
        String decoded = new String(Base64.getDecoder().decode("dGl0bGUgQXV0aGVudGljYXRpb24gU2VxdWVuY2UKCkFsaWNlLT5Cb2I6ABUQUmVxdWVzdApub3RlIHJpZ2h0IG9mIAAlBUJvYiB0aGlua3MgYWJvdXQgaXQKQm9iLT4ASgUANxNzcG9uc2UK"));
        System.out.println(decoded);

        for (char c : decoded.toCharArray()) {
            System.out.println(c + " -> " + (int)c);
        }

        String encoded = new WebSequenceDiagramsEncoder().encode(
                "title Authentication Sequence\n" +
                        "\n" +
                        "Alice->Bob: Authentication Request\n" +
                        "note right of Bob: Bob thinks about it\n" +
                        "Bob->Alice: Authentication Response\n");

        System.out.println(encoded);

        assertEquals("dGl0bGUgQXV0aGVudGljYXRpb24gU2VxdWVuY2UKCkFsaWNlLT5Cb2I6ABUQUmVxdWVzdApub3RlIHJpZ2h0IG9mIAAlBUJvYiB0aGlua3MgYWJvdXQgaXQKQm9iLT4ASgUANxNzcG9uc2UK", encoded);
    }

}