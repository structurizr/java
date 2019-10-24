package com.structurizr.io.mermaid;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class MermaidEncoder {

    public String encode(String mermaidDefinition) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new MermaidCode(mermaidDefinition));
        String base64 = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

        // this encoded version is used in URLs so:
        // - strip trailing = characters
        base64 = base64.replaceAll("=", "");

        // and switch + characters
        base64 = base64.replaceAll("\\+", "-");

        return base64;
    }

}

class MermaidCode {

    private String code;
    private Mermaid mermaid = new Mermaid();

    public MermaidCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Mermaid getMermaid() {
        return mermaid;
    }

}

class Mermaid {

    private String theme = "default";

    public String getTheme() {
        return theme;
    }

}