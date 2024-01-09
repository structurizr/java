package com.structurizr.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Uses a secret to create a message hash.
 */
final class HashBasedMessageAuthenticationCode {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private String secret;

    HashBasedMessageAuthenticationCode(String secret) {
        this.secret = secret;
    }

    String generate(String content) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(content.getBytes());
        return DatatypeConverter.printHexBinary(rawHmac).toLowerCase();
    }

}