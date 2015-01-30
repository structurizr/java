package com.structurizr.api;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HashBasedMessageAuthenticationCode {

    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private String apiKey;
    private String apiSecret;

    public HashBasedMessageAuthenticationCode(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public String generate(String httpMethod, String contentMd5, String contentType, String date, String path) throws Exception {
//        System.out.println("HTTP Method: " + httpMethod);
//        System.out.println("Content-MD5: " + contentMd5);
//        System.out.println("Content-Type: " + contentType);
//        System.out.println("Date: " + date);
//        System.out.println("Path: " + path);

        StringBuilder data = new StringBuilder();
        data.append(httpMethod);
        data.append("\n");
        data.append(contentMd5);
        data.append("\n");
        data.append(contentType);
        data.append("\n");
        data.append(date);
        data.append("\n");
        data.append(path);

//        System.out.println("---");
//        System.out.println(data);
//        System.out.println("---");

        return apiKey + ":" + calculateHmac(data.toString());
    }

    private String calculateHmac(String data) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(apiSecret.getBytes(), HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(rawHmac);
    }

}
