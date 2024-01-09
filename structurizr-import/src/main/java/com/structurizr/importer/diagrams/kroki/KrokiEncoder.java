package com.structurizr.importer.diagrams.kroki;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.Deflater;

/**
 * See https://docs.kroki.io/kroki/setup/encode-diagram/#java
 */
class KrokiEncoder {

    public String encode(String decoded) throws IOException {
        byte[] bytes = Base64.getUrlEncoder().encode(compress(decoded.getBytes()));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private byte[] compress(byte[] source) throws IOException {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(source);
        deflater.finish();

        byte[] buffer = new byte[2048];
        int compressedLength = deflater.deflate(buffer);
        byte[] result = new byte[compressedLength];
        System.arraycopy(buffer, 0, result, 0, compressedLength);
        return result;
    }

}