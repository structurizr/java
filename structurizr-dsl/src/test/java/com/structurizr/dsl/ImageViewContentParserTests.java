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
        assertEquals("System Landscape", imageView.getTitle());
        assertEquals("https://plantuml.com/plantuml/svg/ZLBBJiCm4BpxArRb32rLuH2IgE4b3kL243q01pVU9bOJRsHlr0VYtt6Qj1n0Y9LihMTjpth6KyVISbELWZMN2A7JEmp6apZTEiOAPj8ebyaQms5RYT_CSSSjkipgcZMPlYY4GmQ7jRIIoO8XWuAf1YPO43DLeBJ5h3qY2gqGF8T5ucsDGeIEjwM_1C0ICNpu1E1QPglSKcFK3PLa0pXPxkDgNxqdmmTyieyM__HZE8Ix4Yiqx1TdVNhwDD-KY_bBev8e-XV1J1lyIT3XQTjk0ADlvBdGsSgWSm6C_sgmmzDMHnZto0DPlVEeB9DIvwPjDu3CpsYx3MaX5QsroO-KZtAZgwQQQyL509EBKVTuRqOdf6YbbYRtjWwYA3bOTtuPlwQqvBMq29tDxxs10mZ3txIAOv0E4Y6cQ9J_B5y0", imageView.getContent());
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
        assertEquals("https://mermaid.ink/svg/pako:eJxtkMtqwzAQRX9lmFK8cWgChYKaGNp1d-4uzkKxRraIHkaaNE1D_r12lEJfs5qBM4fLPQG2QREK7KIcenh9bjyANX5X89ESKNJybxm0sVbc6Ms0fmLSfptflJHj4mDdYH1MTA5epFeplQM1uJnQEc6yK_ldViaOYUc_3QCL0bZU5i1_rgodPM8OZLqeBWyDVUX1tLwbgeoPlcwHCXiY3z6Ck7EzfsZhEDAf3otqXQfNBxkJctRNdvzKufg_4f1lyjbYEL-unJe8whLQUXTSKBQn5J7c1Oq1PzyfPwHoMXnQ", imageView.getContent());
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