package com.structurizr.api;

import java.util.Base64;

public class HmacAuthorizationHeader {

    private String apiKey;
    private String hmac;

    public HmacAuthorizationHeader(String apiKey, String hmac) {
        this.apiKey = apiKey;
        this.hmac = hmac;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getHmac() {
        return hmac;
    }

    public String format() {
        return apiKey + ":" + Base64.getEncoder().encodeToString(hmac.getBytes());
    }

    public static HmacAuthorizationHeader parse(String s) {
        String apiKey = s.split(":")[0];
        String hmac = new String(Base64.getDecoder().decode(s.split(":")[1]));

        return new HmacAuthorizationHeader(apiKey, hmac);
    }

}
