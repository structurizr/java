package com.structurizr.importer.diagrams.kroki;

import com.structurizr.Workspace;
import com.structurizr.http.HttpClient;
import com.structurizr.view.ImageView;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class KrokiImporterTests {

    @Test
    public void importDiagram() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
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
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_FORMAT_PROPERTY, "png");
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
    @Tag("IntegrationTest")
    public void importDiagram_AsInlinePNG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_FORMAT_PROPERTY, "png");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_INLINE_PROPERTY, "true");
        ImageView view = workspace.getViews().createImageView("key");

        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");

        new KrokiImporter(httpClient).importDiagram(view, "graphviz", new File("./src/test/resources/diagrams/kroki/diagram.dot"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("diagram.dot", view.getTitle());
        assertEquals("data:image/png;base64,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", view.getContent());
        assertEquals("image/png", view.getContentType());
    }

    @Test
    public void importDiagram_AsSVG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_FORMAT_PROPERTY, "svg");
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
    @Tag("IntegrationTest")
    public void importDiagram_AsInlineSVG() throws Exception {
        Workspace workspace = new Workspace("Name", "Description");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_FORMAT_PROPERTY, "svg");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_INLINE_PROPERTY, "true");
        ImageView view = workspace.getViews().createImageView("key");

        HttpClient httpClient = new HttpClient();
        httpClient.allow(".*");

        new KrokiImporter(httpClient).importDiagram(view, "graphviz", new File("./src/test/resources/diagrams/kroki/diagram.dot"));
        assertEquals("key", view.getKey());
        assertNull(view.getElement());
        assertNull(view.getElementId());
        assertEquals("diagram.dot", view.getTitle());
        assertEquals("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9Im5vIj8+CjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIKICJodHRwOi8vd3d3LnczLm9yZy9HcmFwaGljcy9TVkcvMS4xL0RURC9zdmcxMS5kdGQiPgo8IS0tIEdlbmVyYXRlZCBieSBncmFwaHZpeiB2ZXJzaW9uIDkuMC4wICgyMDIzMDkxMS4xODI3KQogLS0+CjwhLS0gVGl0bGU6IEcgUGFnZXM6IDEgLS0+Cjxzdmcgd2lkdGg9Ijg5cHQiIGhlaWdodD0iMTE2cHQiCiB2aWV3Qm94PSIwLjAwIDAuMDAgODkuMzcgMTE2LjAwIiB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIj4KPGcgaWQ9ImdyYXBoMCIgY2xhc3M9ImdyYXBoIiB0cmFuc2Zvcm09InNjYWxlKDEgMSkgcm90YXRlKDApIHRyYW5zbGF0ZSg0IDExMikiPgo8dGl0bGU+RzwvdGl0bGU+Cjxwb2x5Z29uIGZpbGw9IndoaXRlIiBzdHJva2U9Im5vbmUiIHBvaW50cz0iLTQsNCAtNCwtMTEyIDg1LjM3LC0xMTIgODUuMzcsNCAtNCw0Ii8+CjwhLS0gSGVsbG8gLS0+CjxnIGlkPSJub2RlMSIgY2xhc3M9Im5vZGUiPgo8dGl0bGU+SGVsbG88L3RpdGxlPgo8ZWxsaXBzZSBmaWxsPSJub25lIiBzdHJva2U9ImJsYWNrIiBjeD0iNDAuNjkiIGN5PSItOTAiIHJ4PSIzNy41MyIgcnk9IjE4Ii8+Cjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjQwLjY5IiB5PSItODUuMzMiIGZvbnQtZmFtaWx5PSJUaW1lcyxzZXJpZiIgZm9udC1zaXplPSIxNC4wMCI+SGVsbG88L3RleHQ+CjwvZz4KPCEtLSBXb3JsZCAtLT4KPGcgaWQ9Im5vZGUyIiBjbGFzcz0ibm9kZSI+Cjx0aXRsZT5Xb3JsZDwvdGl0bGU+CjxlbGxpcHNlIGZpbGw9Im5vbmUiIHN0cm9rZT0iYmxhY2siIGN4PSI0MC42OSIgY3k9Ii0xOCIgcng9IjQwLjY5IiByeT0iMTgiLz4KPHRleHQgdGV4dC1hbmNob3I9Im1pZGRsZSIgeD0iNDAuNjkiIHk9Ii0xMy4zMiIgZm9udC1mYW1pbHk9IlRpbWVzLHNlcmlmIiBmb250LXNpemU9IjE0LjAwIj5Xb3JsZDwvdGV4dD4KPC9nPgo8IS0tIEhlbGxvJiM0NTsmZ3Q7V29ybGQgLS0+CjxnIGlkPSJlZGdlMSIgY2xhc3M9ImVkZ2UiPgo8dGl0bGU+SGVsbG8mIzQ1OyZndDtXb3JsZDwvdGl0bGU+CjxwYXRoIGZpbGw9Im5vbmUiIHN0cm9rZT0iYmxhY2siIGQ9Ik00MC42OSwtNzEuN0M0MC42OSwtNjQuNDEgNDAuNjksLTU1LjczIDQwLjY5LC00Ny41NCIvPgo8cG9seWdvbiBmaWxsPSJibGFjayIgc3Ryb2tlPSJibGFjayIgcG9pbnRzPSI0NC4xOSwtNDcuNjIgNDAuNjksLTM3LjYyIDM3LjE5LC00Ny42MiA0NC4xOSwtNDcuNjIiLz4KPC9nPgo8L2c+Cjwvc3ZnPgo=", view.getContent());
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
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_URL_PROPERTY, "https://kroki.io");
        workspace.getViews().getConfiguration().addProperty(KrokiImporter.KROKI_FORMAT_PROPERTY, "jpg");
        ImageView view = workspace.getViews().createImageView("key");

        try {
            new KrokiImporter().importDiagram(view, "graphviz", "...");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Expected a format of png or svg", e.getMessage());
        }
    }

}