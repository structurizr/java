package com.structurizr.io.websequencediagrams;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class WebSequenceDiagramsEncoder {

    public String encode(String webSequenceDiagramsDefinition) throws Exception {
        String base64 = Base64.getEncoder().encodeToString(webSequenceDiagramsDefinition.getBytes(StandardCharsets.UTF_8));

        // this encoded version is used in URLs so:
        // - strip trailing = characters
        base64 = base64.replaceAll("=", "");

        // and switch + characters
        base64 = base64.replaceAll("\\+", "-");

        return base64;
    }

}