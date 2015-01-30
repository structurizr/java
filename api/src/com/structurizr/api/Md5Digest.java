package com.structurizr.api;

import java.security.MessageDigest;
import java.util.Base64;

public class Md5Digest {

    private static final String ALGORITHM = "MD5";

    public String generate(String content) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        return Base64.getEncoder().encodeToString(digest.digest(content.getBytes()));
    }

}