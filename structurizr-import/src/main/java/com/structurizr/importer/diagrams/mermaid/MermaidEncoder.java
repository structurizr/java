package com.structurizr.importer.diagrams.mermaid;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 *  Encodes a Mermaid diagram definition to base64 format, for use with image URLs, etc.
 */
public final class MermaidEncoder {

    private static final String TEMPLATE = "{ \"code\":\"%s\", \"mermaid\":{\"theme\":\"default\"}}";

    public String encode(String mermaidDefinition) {
        return this.encode(mermaidDefinition, false);
    }

    public String encode(String mermaidDefinition, boolean compress) {
        if (compress) {
            try {
                String content = String.format(TEMPLATE, mermaidDefinition.replaceAll("\n", "\\\\n").replaceAll("\"", "\\\\\""));
                byte[] compressedDefinition = compress(content);
                return "pako:" + Base64.getUrlEncoder().encodeToString(compressedDefinition);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return Base64.getUrlEncoder().encodeToString(mermaidDefinition.getBytes(StandardCharsets.UTF_8));
    }

    private byte[] compress(String content) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater();

        DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater, true);
        dos.write(content.getBytes(StandardCharsets.UTF_8));
        dos.finish();

        return baos.toByteArray();
    }

}