package com.structurizr.api;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

final class Md5Digest {

    private static final String ALGORITHM = "MD5";

    String generate(String content) throws Exception {
        if (content == null) {
            content = "";
        }

        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        return DatatypeConverter.printHexBinary(digest.digest(content.getBytes("UTF-8"))).toLowerCase();
    }

}