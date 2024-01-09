package com.structurizr.importer.diagrams.kroki;

import com.structurizr.Workspace;
import com.structurizr.view.ImageView;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class KrokiImporterTests {

    @Test
    public void importDiagram() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty("kroki.url", "https://kroki.io");
        ImageView view = workspace.getViews().createImageView("key");

        new KrokiImporter().importDiagram(view, "graphviz", new File("./src/test/resources/diagrams/kroki/diagram.dot"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("diagram.dot", view.getTitle());
        assertEquals("https://kroki.io/graphviz/png/eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ", view.getContent());
        assertEquals("image/png", view.getContentType());
    }

    @Test
    public void importDiagram_AsPNG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty("kroki.url", "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty("kroki.format", "png");
        ImageView view = workspace.getViews().createImageView("key");

        new KrokiImporter().importDiagram(view, "graphviz", new File("./src/test/resources/diagrams/kroki/diagram.dot"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("diagram.dot", view.getTitle());
        assertEquals("https://kroki.io/graphviz/png/eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ", view.getContent());
        assertEquals("image/png", view.getContentType());
    }

    @Test
    public void importDiagram_AsSVG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty("kroki.url", "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty("kroki.format", "svg");
        ImageView view = workspace.getViews().createImageView("key");

        new KrokiImporter().importDiagram(view, "graphviz", new File("./src/test/resources/diagrams/kroki/diagram.dot"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("diagram.dot", view.getTitle());
        assertEquals("https://kroki.io/graphviz/svg/eNpLyUwvSizIUHBXqPZIzcnJ17ULzy_KSanlAgB1EAjQ", view.getContent());
        assertEquals("image/svg+xml", view.getContentType());
    }

    @Test
    public void importDiagram_WhenTheKrokiUrlIsNotDefined() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        ImageView view = workspace.getViews().createImageView("key");

        try {
            new KrokiImporter().importDiagram(view, "graphviz", new File("./src/test/resources/diagrams/kroki/diagram.dot"));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Please define a view/viewset property named kroki.url to specify your Kroki server", e.getMessage());
        }
    }

    @Test
    public void importDiagram_WhenAnInvalidFormatIsSpecified() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty("kroki.url", "https://mermaid.ink");
        workspace.getViews().getConfiguration().addProperty("kroki.format", "jpg");
        ImageView view = workspace.getViews().createImageView("key");

        try {
            new KrokiImporter().importDiagram(view, "graphviz", "...");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Expected a format of png or svg", e.getMessage());
        }
    }

}