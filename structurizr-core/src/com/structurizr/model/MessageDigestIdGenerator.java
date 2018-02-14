package com.structurizr.model;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.lang.Math.min;

/**
 * An ID generator that uses a digest function when generating IDs for model elements and relationships.
 * This allows IDs to be more stable than a sequential number, and allows models to be merged more easily.
 */
public class MessageDigestIdGenerator implements IdGenerator {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    public static MessageDigestIdGenerator getInstance(final String algorithm) {
        return getInstance(algorithm, Integer.MAX_VALUE);
    }

    public static MessageDigestIdGenerator getInstance(final String algorithm, final int length) {
        try {
            return new MessageDigestIdGenerator(MessageDigest.getInstance(algorithm), length);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Unknown algorithm: " + algorithm, e);
        }
    }

    private final MessageDigest digest;
    private final int length;

    public MessageDigestIdGenerator(final MessageDigest digest, final int length) {
        this.digest = digest;
        this.length = length;
    }

    @Override
    public void found(String id) {
        // nothing to do
    }

    @Override
    public String generateId(Element element) {
        return generateId(
                element.getCanonicalName());
    }

    @Override
    public String generateId(Relationship relationship) {
        return generateId(
                relationship.getSource().getCanonicalName(),
                relationship.getDescription(),
                relationship.getDestination().getCanonicalName());
    }

    public synchronized String generateId(final String...terms) {
        digest.reset();
        for (final String term : terms) {
            if(term!=null) {
                digest.update(term.getBytes(UTF8));
            }
        }

        final byte[] bytes = this.digest.digest();
        final char[] chars = new char[min(bytes.length * 2, this.length)];

        int b=0, c=0;
        while(b < bytes.length && c < chars.length) {
            int value = bytes[b++] & 0xFF;
            chars[c++] = HEX_CHARS[value >>> 4];
            if(c < chars.length) {
                chars[c++] = HEX_CHARS[value & 0x0F];
            }
        }
        return new String(chars);
    }
}
