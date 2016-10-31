package com.structurizr.api;

import java.util.Base64;

final class HmacAuthorizationHeader {

    private String apiKey;
    private String hmac;

    HmacAuthorizationHeader(String apiKey, String hmac) {
        this.apiKey = apiKey;
        this.hmac = hmac;
    }

    String getApiKey() {
        return apiKey;
    }

    String getHmac() {
        return hmac;
    }

    String format() {
        return apiKey + ":" + Base64.getEncoder().encodeToString(hmac.getBytes());
    }

    static HmacAuthorizationHeader parse(String s) {
        String apiKey = s.split(":")[0];
        String hmac = new String(Base64.getDecoder().decode(s.split(":")[1]));

        return new HmacAuthorizationHeader(apiKey, hmac);
    }

}
