package com.structurizr.dsl;

import com.structurizr.importer.diagrams.kroki.KrokiImporter;
import com.structurizr.importer.diagrams.mermaid.MermaidImporter;
import com.structurizr.importer.diagrams.plantuml.PlantUMLImporter;
import com.structurizr.view.ColorScheme;
import com.structurizr.view.ImageView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
            parser = new ImageViewContentParser();
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
            parser = new ImageViewContentParser();
            parser.parsePlantUML(context, null, tokens("plantuml", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("plantuml <file> is not permitted (feature structurizr.feature.dsl.filesystem is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parsePlantUML_ThrowsAnException_WhenUsingAHttpsUrlAndHttpsIsNotEnabled() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            context.getFeatures().disable(Features.HTTPS);
            parser = new ImageViewContentParser();
            parser.parsePlantUML(context, null, tokens("plantuml", "https://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Image views via HTTPS are not permitted (feature structurizr.feature.dsl.https is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parsePlantUML_ThrowsAnException_WhenUsingAHttpUrlAndHttpIsNotEnabled() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            context.getFeatures().disable(Features.HTTP);
            parser = new ImageViewContentParser();
            parser.parsePlantUML(context, null, tokens("plantuml", "http://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Image views via HTTP are not permitted (feature structurizr.feature.dsl.http is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parsePlantUML_Source() {
        String source = """
                @startuml
                Bob -> Alice : hello
                @enduml""";

        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        parser = new ImageViewContentParser();
        parser.parsePlantUML(new ImageViewDslContext(imageView), null, tokens("plantuml", source));
        assertEquals("https://plantuml.com/plantuml/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", imageView.getContent());
    }

    @Test
    void test_parsePlantUML_Source_Light() {
        String source = """
                @startuml
                Bob -> Alice : hello
                @enduml""";

        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Light);
        parser.parsePlantUML(context, null, tokens("plantuml", source));
        assertEquals("https://plantuml.com/plantuml/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", imageView.getContentLight());
    }

    @Test
    void test_parsePlantUML_Source_Dark() {
        String source = """
                @startuml
                Bob -> Alice : hello
                @enduml""";

        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Dark);
        parser.parsePlantUML(context, null, tokens("plantuml", source));
        assertEquals("https://plantuml.com/plantuml/svg/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9vt98pKi1IW80", imageView.getContentDark());
    }

    @Test
    void test_parsePlantUML_WithViewKey() {
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setWorkspace(workspace);
        workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description").addAllElements();
        workspace.getViews().getConfiguration().addProperty(PlantUMLImporter.PLANTUML_URL_PROPERTY, "https://plantuml.com/plantuml");

        parser = new ImageViewContentParser();
        parser.parsePlantUML(context, null, tokens("plantuml", "SystemLandscape"));
        assertEquals("System Landscape View", imageView.getTitle());
        assertEquals("Description", imageView.getDescription());
        assertNull(imageView.getContent());
        assertEquals("https://plantuml.com/plantuml/svg/ZLBBJiCm4BpxArPmXfQgS0X9rF0IXt8Xg3q01pVP9bOTRsHlr0VYtt6Qj1n0Y9LihMTjpth64yVISbDfmOerGkZK3eFHE4wtZh62gJIvosIDC5Eu3WTjENupnsrtw3AhQbPa-g8G3XaSrj9A9Wk630gc6fXWGSnKGQuiPkqHKQeSmHDP9DxMA4JeUAlz9G2MYE739m0tCbiLbXgJtv8c6y3fSX_N--e36JxWutsq-ASVWm7SQwpGi5-Sz-dPytoZ5_DPaoTHz2-2gJBuaw33qxRT08RVo4kfifL1vm8O_TLWXwUjZZ3gaKUoQkTHgHEj2jEs6q3cPxJTXhIKEQsLAOwKJtAZggQQgvpB0CQNm-xntenEID5ABKtXlJs9ekHWtSLL_9hIajVI8dHUl_S6da0O_gPL78Dqa0WnGPFx7_C5", imageView.getContentLight());
        assertEquals("https://plantuml.com/plantuml/svg/ZLBBJiCm4BpxArRb37seS0X9rF0IXt8Xg3q01pTU4gkEDxAtwWFnxpXDAGSGOYLRwrdRivxnnBDqlAgDOCq68VPwXz5edEPRprZ3L5hb2zaWp3IkutvRJb_iSTiD-iBfXZNPGr48ZmmU6-aaamDB5WLJ0qom86QgGMc7HNj4L5eX12A7nDi6XOWzRqsu1C0HCRo71E1A5ilIqSggQpBa8ZWPxkDoNxqZorzuiOyM_mYZtuTRWpLQ3ekpGthwED-OnNosKbcI_8jWgYt-9EZml6qtWi4tybJfOcdH-mX6VpNOuNch8up67N9FJky2AarcT6dRTYCemeoksv1NKj5Qs_98-I0tkbxLSwsuYc1yFkWU7ypeX1IjrDAMmTjUacHVrWqlqkUStdWj7KBdzUl1m1x4yMzQfIb83vaG4xGg_9XF", imageView.getContentDark());
    }

    @Test
    void test_parseMermaid_ThrowsAnException_WithTooFewTokens() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            parser = new ImageViewContentParser();
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
            parser = new ImageViewContentParser();
            parser.parseMermaid(context, null, tokens("mermaid", "image.mmd"));
            fail();
        } catch (Exception e) {
            assertEquals("mermaid <file> is not permitted (feature structurizr.feature.dsl.filesystem is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parseMermaid_ThrowsAnException_WhenUsingAHttpsUrlAndHttpsIsNotEnabled() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            context.getFeatures().disable(Features.HTTPS);
            parser = new ImageViewContentParser();
            parser.parseMermaid(context, null, tokens("mermaid", "https://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Image views via HTTPS are not permitted (feature structurizr.feature.dsl.https is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parseMermaid_ThrowsAnException_WhenUsingAHttpUrlAndHttpIsNotEnabled() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            context.getFeatures().disable(Features.HTTP);
            parser = new ImageViewContentParser();
            parser.parseMermaid(context, null, tokens("mermaid", "http://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Image views via HTTP are not permitted (feature structurizr.feature.dsl.http is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parseMermaid_Source() {
        String source = """
                flowchart TD
                    A[Christmas] -->|Get money| B(Go shopping)
                    B --> C{Let me think}
                    C -->|One| D[Laptop]
                    C -->|Two| E[iPhone]
                    C -->|Three| F[fa:fa-car Car]""";

        workspace.getViews().getConfiguration().addProperty(MermaidImporter.MERMAID_URL_PROPERTY, "https://mermaid.ink");
        parser = new ImageViewContentParser();
        parser.parseMermaid(new ImageViewDslContext(imageView), null, tokens("mermaid", source));
        assertEquals("https://mermaid.ink/svg/pako:eJxVj70OgjAUhV_lppMm8gIMJlKUhUQHtspwAxfbSH9SaoihvLsgi571-85JzgSssS2xlHW9HRuJPkCV3w0sOQkuvRqCxqGGJDnGggJoa-gdIdsVFgZpnVPmsd_8bJWAT-WqEQSpzHPeEP_2r4Yi5KJEF6yrf0k12ghnoW5ymf8n0tPSuogO0w6TBj1w9DU7ANPkNaqWpRMLkvR6oqUOX31g8_wBLY9E1w==", imageView.getContent());
    }

    @Test
    void test_parseMermaid_Source_Light() {
        String source = """
                flowchart TD
                    A[Christmas] -->|Get money| B(Go shopping)
                    B --> C{Let me think}
                    C -->|One| D[Laptop]
                    C -->|Two| E[iPhone]
                    C -->|Three| F[fa:fa-car Car]""";

        workspace.getViews().getConfiguration().addProperty(MermaidImporter.MERMAID_URL_PROPERTY, "https://mermaid.ink");
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Light);
        parser.parseMermaid(context, null, tokens("mermaid", source));
        assertEquals("https://mermaid.ink/svg/pako:eJxVj70OgjAUhV_lppMm8gIMJlKUhUQHtspwAxfbSH9SaoihvLsgi571-85JzgSssS2xlHW9HRuJPkCV3w0sOQkuvRqCxqGGJDnGggJoa-gdIdsVFgZpnVPmsd_8bJWAT-WqEQSpzHPeEP_2r4Yi5KJEF6yrf0k12ghnoW5ymf8n0tPSuogO0w6TBj1w9DU7ANPkNaqWpRMLkvR6oqUOX31g8_wBLY9E1w==", imageView.getContentLight());
    }

    @Test
    void test_parseMermaid_Source_Dark() {
        String source = """
                flowchart TD
                    A[Christmas] -->|Get money| B(Go shopping)
                    B --> C{Let me think}
                    C -->|One| D[Laptop]
                    C -->|Two| E[iPhone]
                    C -->|Three| F[fa:fa-car Car]""";

        workspace.getViews().getConfiguration().addProperty(MermaidImporter.MERMAID_URL_PROPERTY, "https://mermaid.ink");
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Dark);
        parser.parseMermaid(context, null, tokens("mermaid", source));
        assertEquals("https://mermaid.ink/svg/pako:eJxVj70OgjAUhV_lppMm8gIMJlKUhUQHtspwAxfbSH9SaoihvLsgi571-85JzgSssS2xlHW9HRuJPkCV3w0sOQkuvRqCxqGGJDnGggJoa-gdIdsVFgZpnVPmsd_8bJWAT-WqEQSpzHPeEP_2r4Yi5KJEF6yrf0k12ghnoW5ymf8n0tPSuogO0w6TBj1w9DU7ANPkNaqWpRMLkvR6oqUOX31g8_wBLY9E1w==", imageView.getContentDark());
    }

    @Test
    void test_parseMermaid_WithViewKey() {
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setWorkspace(workspace);
        workspace.getModel().addSoftwareSystem("A");
        workspace.getViews().createSystemLandscapeView("SystemLandscape", "Description").addAllElements();
        workspace.getViews().getConfiguration().addProperty(MermaidImporter.MERMAID_URL_PROPERTY, "https://mermaid.ink");

        parser = new ImageViewContentParser();
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
            parser = new ImageViewContentParser();
            parser.parseKroki(context, null, tokens("kroki"));
            fail();
        } catch (Exception e) {
            assertEquals("Expected: kroki <format> <source|file|url>", e.getMessage());
        }
    }

    @Test
    void test_parseKroki_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            parser = new ImageViewContentParser();
            parser.parseKroki(new ImageViewDslContext(imageView), null, tokens("kroki", "plantuml", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("kroki plantuml <file> is not permitted (feature structurizr.feature.dsl.filesystem is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parseKroki_ThrowsAnException_WhenUsingAHttpsUrlAndHttpsIsNotEnabled() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            context.getFeatures().disable(Features.HTTPS);
            parser = new ImageViewContentParser();
            parser.parseKroki(context, null, tokens("kroki", "plantuml", "https://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Image views via HTTPS are not permitted (feature structurizr.feature.dsl.https is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parseKroki_ThrowsAnException_WhenUsingAHttpUrlAndHttpIsNotEnabled() {
        try {
            ImageViewDslContext context = new ImageViewDslContext(imageView);
            context.setWorkspace(workspace);
            context.getFeatures().disable(Features.HTTP);
            parser = new ImageViewContentParser();
            parser.parseKroki(context, null, tokens("kroki", "plantuml", "http://example.com"));
            fail();
        } catch (Exception e) {
            assertEquals("Image views via HTTP are not permitted (feature structurizr.feature.dsl.http is not enabled)", e.getMessage());
        }
    }

    @Test
    void test_parseKroki_Source() {
        String source = """
                flowchart TD
                    A[Christmas] -->|Get money| B(Go shopping)
                    B --> C{Let me think}
                    C -->|One| D[Laptop]
                    C -->|Two| E[iPhone]
                    C -->|Three| F[fa:fa-car Car]""";

        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        parser = new ImageViewContentParser();
        parser.parseKroki(new ImageViewDslContext(imageView), null, tokens("kroki", "mermaid", source));
        assertEquals("https://kroki.io/mermaid/png/eNpVjLEOwiAURXe_4o068AMOJpZqlyZ16EYYXhrwES2PAEljxH-XdtK7nnOuffIyEcYMY7uDurOSFF3KMyYNQpxKZzLM7M2rQLPvGBJxCM7fD5verA7Id79aBjI5__hsRG714E2BVvUYMgf9A8aFC1yUu1H9_gMUTW2uyuLRopgwgsSovzbHM0c=", imageView.getContent());
    }

    @Test
    void test_parseKroki_Source_Light() {
        String source = """
                flowchart TD
                    A[Christmas] -->|Get money| B(Go shopping)
                    B --> C{Let me think}
                    C -->|One| D[Laptop]
                    C -->|Two| E[iPhone]
                    C -->|Three| F[fa:fa-car Car]""";

        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Light);
        parser.parseKroki(context, null, tokens("kroki", "mermaid", source));
        assertEquals("https://kroki.io/mermaid/png/eNpVjLEOwiAURXe_4o068AMOJpZqlyZ16EYYXhrwES2PAEljxH-XdtK7nnOuffIyEcYMY7uDurOSFF3KMyYNQpxKZzLM7M2rQLPvGBJxCM7fD5verA7Id79aBjI5__hsRG714E2BVvUYMgf9A8aFC1yUu1H9_gMUTW2uyuLRopgwgsSovzbHM0c=", imageView.getContentLight());
    }

    @Test
    void test_parseKroki_Source_Dark() {
        String source = """
                flowchart TD
                    A[Christmas] -->|Get money| B(Go shopping)
                    B --> C{Let me think}
                    C -->|One| D[Laptop]
                    C -->|Two| E[iPhone]
                    C -->|Three| F[fa:fa-car Car]""";

        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Dark);
        parser.parseKroki(context, null, tokens("kroki", "mermaid", source));
        assertEquals("https://kroki.io/mermaid/png/eNpVjLEOwiAURXe_4o068AMOJpZqlyZ16EYYXhrwES2PAEljxH-XdtK7nnOuffIyEcYMY7uDurOSFF3KMyYNQpxKZzLM7M2rQLPvGBJxCM7fD5verA7Id79aBjI5__hsRG714E2BVvUYMgf9A8aFC1yUu1H9_gMUTW2uyuLRopgwgsSovzbHM0c=", imageView.getContentDark());
    }

    @Test
    void test_parseImage_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            parser = new ImageViewContentParser();
            parser.parseImage(new ImageViewDslContext(imageView), null, tokens("image", "image.png"));
            fail();
        } catch (Exception e) {
            assertEquals("image <file> is not permitted (feature structurizr.feature.dsl.filesystem is not enabled)", e.getMessage());
        }
    }

    @Test
    @Tag("IntegrationTest")
    void test_parseImage() {
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.getFeatures().enable(Features.HTTPS);
        context.getHttpClient().allow(".*");

        parser.parseImage(context, null, tokens("image", "https://static.structurizr.com/img/structurizr-banner.png"));
        assertEquals("https://static.structurizr.com/img/structurizr-banner.png", imageView.getContent());
    }

    @Test
    @Tag("IntegrationTest")
    void test_parseImage_Url_Light() {
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Light);
        context.getFeatures().enable(Features.HTTPS);
        context.getHttpClient().allow(".*");

        parser.parseImage(context, null, tokens("image", "https://static.structurizr.com/img/structurizr-banner.png"));
        assertEquals("https://static.structurizr.com/img/structurizr-banner.png", imageView.getContentLight());
    }

    @Test
    @Tag("IntegrationTest")
    void test_parseImage_Url_Dark() {
        parser = new ImageViewContentParser();
        ImageViewDslContext context = new ImageViewDslContext(imageView);
        context.setColorScheme(ColorScheme.Dark);
        context.getFeatures().enable(Features.HTTPS);
        context.getHttpClient().allow(".*");

        parser.parseImage(context, null, tokens("image", "https://static.structurizr.com/img/structurizr-banner.png"));
        assertEquals("https://static.structurizr.com/img/structurizr-banner.png", imageView.getContentDark());
    }

}