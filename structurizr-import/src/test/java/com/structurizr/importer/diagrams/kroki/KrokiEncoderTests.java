package com.structurizr.importer.diagrams.kroki;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KrokiEncoderTests {

    @Test
    public void encode_plantuml() throws Exception {
        assertEquals("eNpzKC5JLCopzc3hcspPUtC1U3DMyUxOVbBSyEjNycnnckjNSwFKAgD4CQzA",
                new KrokiEncoder().encode("@startuml\n" +
                        "Bob -> Alice : hello\n" +
                        "@enduml"));
    }

    @Test
    public void encode_seqdiag() throws Exception {
        assertEquals("eNorTi1MyUxMV6jmUlBIKsovL04tUlDQtVMoT00CMsuAvOicxKTUHAVbBSV31xAF_WKIBv3isnT9pMTiVDMTpVhroGaEBpD2gqL85NTi4nxk7c75eUDpEoWS1Aogka-QmZuYnoqu2UZXF6HZGslRIAm4MmuuWgA13z1R",
                new KrokiEncoder().encode("seqdiag {\n" +
                        "  browser  -> webserver [label = \"GET /seqdiag/svg/base64\"];\n" +
                        "  webserver  -> processor [label = \"Convert text to image\"];\n" +
                        "  webserver <-- processor;\n" +
                        "  browser <-- webserver;\n" +
                        "}"));
    }

    @Test
    public void encode_erd() throws Exception {
        assertEquals(
                "eNqLDkgtKs7Pi-XSykvMTeXKSM1MzyjhKodQ2kmZRSUZ8Tn5yYklmfl58ZkpXFzRPlAeUAuQn5xZUslVXJJYksqVnF-aV1JUycUFMVJBS1fXUAGmGgCFAiQX",
                new KrokiEncoder().encode("[Person]\n" +
                        "*name\n" +
                        "height\n" +
                        "weight\n" +
                        "+birth_location_id\n" +
                        "\n" +
                        "[Location]\n" +
                        "*id\n" +
                        "city\n" +
                        "state\n" +
                        "country\n" +
                        "\n" +
                        "Person *--1 Location"));
    }

    @Test
    public void encode_graphviz() throws Exception {
        assertEquals(
                "eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ",
                new KrokiEncoder().encode("digraph G {Hello->World}\n"));
    }

    @Test
    public void encode_blockdiag() throws Exception {
        assertEquals(
                "eNpdzDEKQjEQhOHeU4zpPYFoYesRxGJ9bwghMSsbUYJ4d10UCZbDfPynolOek0Q8FsDeNCestoisNLmy-Qg7R3Blcm5hPcr0ITdaB6X15fv-_YdJixo2CNHI2lmK3sPRA__RwV5SzV80ZAegJjXSyfMFptc71w==",
                new KrokiEncoder().encode("blockdiag {\n" +
                "  Kroki -> generates -> \"Block diagrams\";\n" +
                "  Kroki -> is -> \"very easy!\";\n" +
                "\n" +
                "  Kroki [color = \"greenyellow\"];\n" +
                "  \"Block diagrams\" [color = \"pink\"];\n" +
                "  \"very easy!\" [color = \"orange\"];\n" +
                "}"));
    }

}