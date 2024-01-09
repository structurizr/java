package com.structurizr.importer.diagrams.mermaid;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MermaidEncoderTests {

    @Test
    public void encode_flowchart() throws Exception {
        File file = new File("./src/test/resources/diagrams/mermaid/flowchart.mmd");
        String mermaid = Files.readString(file.toPath());
        assertEquals("Zmxvd2NoYXJ0IFRECiAgICBBW0NocmlzdG1hc10gLS0-fEdldCBtb25leXwgQihHbyBzaG9wcGluZykKICAgIEIgLS0-IEN7TGV0IG1lIHRoaW5rfQogICAgQyAtLT58T25lfCBEW0xhcHRvcF0KICAgIEMgLS0-fFR3b3wgRVtpUGhvbmVdCiAgICBDIC0tPnxUaHJlZXwgRltmYTpmYS1jYXIgQ2FyXQ==", new MermaidEncoder().encode(mermaid));
    }

    @Test
    public void encode_class() throws Exception {
        File file = new File("./src/test/resources/diagrams/mermaid/class.mmd");
        String mermaid = Files.readString(file.toPath());
        assertEquals("Y2xhc3NEaWFncmFtCiAgICBBbmltYWwgPHwtLSBEdWNrCiAgICBBbmltYWwgPHwtLSBGaXNoCiAgICBBbmltYWwgPHwtLSBaZWJyYQogICAgQW5pbWFsIDogK2ludCBhZ2UKICAgIEFuaW1hbCA6ICtTdHJpbmcgZ2VuZGVyCiAgICBBbmltYWw6ICtpc01hbW1hbCgpCiAgICBBbmltYWw6ICttYXRlKCkKICAgIGNsYXNzIER1Y2t7CiAgICAgICtTdHJpbmcgYmVha0NvbG9yCiAgICAgICtzd2ltKCkKICAgICAgK3F1YWNrKCkKICAgIH0KICAgIGNsYXNzIEZpc2h7CiAgICAgIC1pbnQgc2l6ZUluRmVldAogICAgICAtY2FuRWF0KCkKICAgIH0KICAgIGNsYXNzIFplYnJhewogICAgICArYm9vbCBpc193aWxkCiAgICAgICtydW4oKQogICAgfQo=", new MermaidEncoder().encode(mermaid));

    }

}