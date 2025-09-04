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
        assertEquals("https://plantuml.com/plantuml/svg/HP2nJiD038RtUmghF00f6oYD6f2OO2eI0p2Od9EUUh4Zdwkq8DwTkrB0bZpylsL_zZePgkt7w18P99fGqKI1XSbPi4YmEIQZ4HwGVUfm8kTC9Z21Tp6J4NnGwYm8EvTsWSk44JuT0AhAV2zic_11iAoovAd7VRGdEbWRmy0ZiK6N2sbsPyNfENZRmbLLkaSyF59AED1vGkM-dDi6Jv2HbCIE1UT_Qm517YBLTTiq9uXRx7Q3ofxzdSHys8K_HNOAsLchJb6wHJtfMRt6abbDM_Go1nwWnvYeGFnjWiLgrRvodJBXpR9gNZRIsupw-xUt-h9OpG9-c311wzoQsEUdVmC0", imageView.getContent());
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
        assertEquals("https://mermaid.ink/svg/pako:eJxlkMtuwjAQRX9lNAhlE9SwqupCpLLuLt0RFiYeJxZ-RLYppYh_bxJHVR93NrM4c3U0N8DGCUKGred9B2-72gJoZU9VvGoCQZKfdQSptGYLOaW2IxPOx3QiFB8WA_saq2uIZOCVWxEa3lONhxEd4FQ2kz_L8hC9O9HvboD10LYR6j1dbjPpbFxdSLVdZHB0WmTly-ZhAMp_VFCfxOCxWD6D4b5VdhVdz6DoP7JyXzkZL9wTJNVD6vjjuZ4NxZRvwyc-Tt447TxbFFOSL1mBOaAhb7gSyG4YOzLjV-f_4f3-BQMfekI=", imageView.getContent());
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