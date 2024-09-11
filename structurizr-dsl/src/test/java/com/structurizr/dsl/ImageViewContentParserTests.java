package com.structurizr.dsl;

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
    void test_parsePlantUML_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            parser = new ImageViewContentParser(true);
            parser.parsePlantUML(new ImageViewDslContext(imageView), null, tokens("plantuml", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("PlantUML source must be specified as a URL when running in restricted mode", e.getMessage());
        }
    }

    @Test
    void test_parseMermaid_ThrowsAnException_WhenUsingAFileNameInRestrictedMode() {
        try {
            parser = new ImageViewContentParser(true);
            parser.parseMermaid(new ImageViewDslContext(imageView), null, tokens("mermaid", "image.puml"));
            fail();
        } catch (Exception e) {
            assertEquals("Mermaid source must be specified as a URL when running in restricted mode", e.getMessage());
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