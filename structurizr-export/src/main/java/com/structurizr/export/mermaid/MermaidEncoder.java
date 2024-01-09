package com.structurizr.export.mermaid;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *  Encodes a Mermaid diagram definition to base64 format, for use with image URLs, etc.
 */
public class MermaidEncoder {

    private static final String TEMPLATE = "{ \"code\":\"%s\", \"mermaid\":{\"theme\":\"default\", \"securityLevel\": \"loose\"}}";

    public String encode(String mermaidDefinition) {
        String s = String.format(TEMPLATE, mermaidDefinition.replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\""));
        return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }

}