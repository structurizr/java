package com.structurizr.dsl;

import com.structurizr.importer.diagrams.mermaid.MermaidImporter;
import com.structurizr.importer.diagrams.plantuml.PlantUMLImporter;
import com.structurizr.view.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ImageViewContentParserTests extends AbstractTests {

    private ImageViewContentParser parser;
    private ImageView imageView;

    @BeforeEach
    void setUp() {
        imageView = workspace.getViews().createImageView("key");
    }

    @Test
    void test_parsePlantUML_ThrowsAnException_WithTooFewTokens() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            parser = new ImageViewContentParser(true);
            parser.parsePlantUML(context, null, tokens("plantuml"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: plantuml <source|file|url|viewKey>", e.getMessage());
        }
    }

    @Test
    void test_parsePlantUML_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            parser = new ImageViewContentParser(true);
            parser.parsePlantUML(context, null, tokens("plantuml", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("PlantUML source must be specified as a URL when running in restricted mode", e.getMessage());
        }
    }

    @Test
    void test_parsePlantUML_WithViewKey() {
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setWorkspace(workspace);
        workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description").addAllElements();
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");

        parser = new ImageViewContentParser(true);
        parser.parsePlantUML(context, null, tokens("plantuml", "SystemLandscape"));
        assertEquals("System Landscape View", imageView.getTitle());
        assertEquals("Description", imageView.getDescription());
        assertEquals("https://plantuml.com/plantuml/svg/ZLBBJiCm4BpxArPmXfQgS0X9rF0IXt8Xg3q01pVP9bOTRsHlr0VYtt6Qj1n0Y9LihMTjpth64yVISbDfmOerGkZK3eFHE4wtZh62gJIvosIDC5Eu3WTjENupnsrtw3AhQbPa-g8G3XaSrj9A9Wk630gc6fXWGSnKGQuiPkqHKQeSmHDP9DxMA4JeUAlz9G2MYE739m0tCbiLbXgJtv8c6y3fSX_N--e36JxWutsq-ASVWm7SQwpGi5-Sz-dPytoZ5_DPaoTHz2-2gJBuaw33qxRT08RVo4kfifL1vm8O_TLWXwUjZZ3gaKUoQkTHgHEj2jEs6q3cPxJTXhIKEQsLAOwKJtAZggQQgvpB0CQNm-xntenEID5ABKtXlJs9ekHWtSLL_9hIajVI8dHUl_S6da0O_gPL78Dqa0WnGPFx7_C5", imageView.getContent());
    }

    @Test
    void test_parseMermaid_ThrowsAnException_WithTooFewTokens() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            parser = new ImageViewContentParser(true);
            parser.parseMermaid(context, null, tokens("mermaid"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: mermaid <source|file|url|viewKey>", e.getMessage());
        }
    }

    @Test
    void test_parseMermaid_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            parser = new ImageViewContentParser(true);
            parser.parseMermaid(context, null, tokens("mermaid", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("Mermaid source must be specified as a URL when running in restricted mode", e.getMessage());
        }
    }

    @Test
    void test_parseMermaid_WithViewKey() {
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setWorkspace(workspace);
        workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description").addAllElements();
        workspace.getViews().getConfiguration().addProperty(MermaidImporter.MERMAID_URL_PROPERTY, "https://mermaid.ink");

        parser = new ImageViewContentParser(true);
        parser.parseMermaid(context, null, tokens("mermaid", "SystemLandscape"));
        assertEquals("System Landscape View", imageView.getTitle());
        assertEquals("Description", imageView.getDescription());
        assertEquals("https://mermaid.ink/svg/pako:eJxtkM1rwkAQxf-VYYrkEqmCUNjaQD33luLFeFizs8nifoTd1WjF_72Ja6Ffc5qB33vzeBfA2glCho3nXQvvq8oCaGX3ZTxrAkGSH3QEqbRmD_I2lR2ZcNgliVB8WAxsKizPIZKBN25FqHlHsFbUV7gd-UGRHO_4d8c8RO_29PMBwHywXAp1TMqXTDobpz2ppo0Mdk6LrHhdPg5A8YcK6oMYPM0mz2C4b5SdRtcxmHWnrNiUTsaee4KUd5s8fuWc_59wcZu8dtr5ryvlJSswBzTkDVcC2QVjS2as9l4iXq-fYuV7iw==", imageView.getContent());
    }

    @Test
    void test_parseKroki_ThrowsAnException_WithTooFewTokens() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            parser = new ImageViewContentParser(true);
            parser.parseKroki(context, null, tokens("kroki"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: kroki <format> <source|file|url>", e.getMessage());
        }
    }

    @Test
    void test_parseKroki_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            parser = new ImageViewContentParser(true);
            parser.parseKroki(new ImageViewDslContext(imageView), null, tokens("kroki", "plantuml", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("Kroki source must be specified as a URL when running in restricted mode", e.getMessage());
        }
    }

    @Test
    void test_parseImage_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            parser = new ImageViewContentParser(true);
            parser.parseImage(new ImageViewDslContext(imageView), null, tokens("image", "image.png"));
            fail();
        } catch (Exception e) {
            assertEquals("Images must be specified as a URL when running in restricted mode", e.getMessage());
        }
    }

}