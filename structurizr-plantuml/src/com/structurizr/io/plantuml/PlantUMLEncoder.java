package com.structurizr.io.plantuml;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

/**
 *  A Java implementation of http://plantuml.com/code-javascript-synchronous
 *  that uses Java's built-in Deflate algorithm.
 */
public class PlantUMLEncoder {

    public String encode(String plantUMLDefinition) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);

        DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater, true);
        dos.write(plantUMLDefinition.getBytes(StandardCharsets.UTF_8));
        dos.finish();

        return encode(baos.toByteArray());
    }

    private String encode(byte[] bytes) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < bytes.length; i += 3) {
            int b1 = (bytes[i]) & 0xFF;
            int b2 = (i + 1 < bytes.length ? bytes[i + 1] : (byte)0) & 0xFF;
            int b3 = (i + 2 < bytes.length ? bytes[i + 2] : (byte)0) & 0xFF;

            append3bytes(buf, b1, b2, b3);
        }

        return buf.toString();
    }

    private char encode6bit(byte b) {
        if (b < 10) {
            return (char) ('0' + b);
        }
        b -= 10;
        if (b < 26) {
            return (char) ('A' + b);
        }
        b -= 26;
        if (b < 26) {
            return (char) ('a' + b);
        }
        b -= 26;
        if (b == 0) {
            return '-';
        }
        if (b == 1) {
            return '_';
        }

        return '?';
    }

    private void append3bytes(StringBuilder buf, int b1, int b2, int b3) {
        int c1 = b1 >> 2;
        int c2 = (b1 & 0x3) << 4 | b2 >> 4;
        int c3 = (b2 & 0xF) << 2 | b3 >> 6;
        int c4 = b3 & 0x3F;

        buf.append(encode6bit((byte)(c1 & 0x3F)));
        buf.append(encode6bit((byte)(c2 & 0x3F)));
        buf.append(encode6bit((byte)(c3 & 0x3F)));
        buf.append(encode6bit((byte)(c4 & 0x3F)));
    }

}