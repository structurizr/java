package com.structurizr.importer.diagrams.mermaid;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *  Encodes a Mermaid diagram definition to base64 format, for use with image URLs, etc.
 */
public class MermaidEncoder {

    public String encode(String mermaidDefinition) {
        return Base64.getUrlEncoder().encodeToString(mermaidDefinition.getBytes(StandardCharsets.UTF_8));
    }

}